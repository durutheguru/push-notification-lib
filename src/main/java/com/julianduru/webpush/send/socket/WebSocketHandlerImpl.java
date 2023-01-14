package com.julianduru.webpush.send.socket;

import com.julianduru.webpush.send.UserIdTokenRepository;
import com.julianduru.webpush.send.api.Message;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.sse.UserIDEmittersContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * created by julian on 12/01/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandlerImpl implements WebSocketHandler {


    private final UserIdTokenRepository userIdTokenRepository;


    // mapping of userId to emitters for userId
    private final ConcurrentHashMap<String, UserIDEmittersContainer> socketEmitterMap = new ConcurrentHashMap<>();


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var webSocketQuery = session.getHandshakeInfo().getUri().getRawQuery();
        var tokenId = webSocketQuery.split("=")[1];
        var token = userIdTokenRepository.findByToken(tokenId);
        if (token.isEmpty()) {
            return Mono.empty();
        }

        if (token.get().isExpired()) {
            log.info("Expired Token {}", tokenId);
            return Mono.empty();
        }

        var userId = token.get().userId();
        var publisher = addMapping(userId, tokenId);

        return session.send(
            publisher.map(
                msg -> {
                    log.debug("Received Message from publisher: {}", msg);

                    switch (msg.getMessageType()) {
                        case STRING -> {
                            return session.textMessage(msg.getData().toString());
                        }
                        case BYTES -> {
                            return session.binaryMessage(
                                dataBufferFactory -> dataBufferFactory.wrap((ByteBuffer) msg.getData())
                            );
                        }
                        default -> {
                            return session.textMessage("..");
                        }
                    }
                }
            )
        ).and(
            session.receive()
                .doOnNext(
                    message -> {
                        switch (message.getType()) {
                            case BINARY -> handleBinaryMessage(session, message);
                            case TEXT -> handleTextMessage(session, message);
                        }
                    }
                )
        );
    }


    private Flux<Message<?>> addMapping(String authUserId, String token) {
        if (!socketEmitterMap.containsKey(authUserId)) {
            this.socketEmitterMap.put(authUserId, new UserIDEmittersContainer());
        }

        return this.socketEmitterMap.get(authUserId).add(token);
    }


    protected void handleTextMessage(WebSocketSession session, WebSocketMessage message) {
        log.info("Text Message Received: {}", message);
    }


    protected void handleBinaryMessage(WebSocketSession session, WebSocketMessage message) {
        log.info("Binary Message Received: {}", message);
    }


    public List<OperationStatus<String>> send(String userId, Message<?> msg) {
        var emittersContainer = socketEmitterMap.get(userId);
        if (emittersContainer == null) {
            log.info("No Emitter found for user {}", userId);
            return List.of(OperationStatus.failure("No connection to user"));
        }

        var sinkList = emittersContainer.allEmitters();
        if (sinkList == null || sinkList.isEmpty()) {
            log.info("No Sink found for user {}", userId);
            return List.of(OperationStatus.failure("No sink to user"));
        }

        return sinkList
            .stream()
            .map(
                sink -> {
                    try {
                        if (sink.currentSubscriberCount() < 1) {
                            throw new IllegalStateException("No subscribers on sink. To be scheduled for removal");
                        }
                        sink.tryEmitNext(msg);
                        return OperationStatus.success("Sent Message");
                    } catch (Exception e) {
                        log.error("Unable to complete emitter Send", e);
                        sink.tryEmitError(e);
                        // TODO: remove dead emitter from list...
                        return OperationStatus.failure(e.getMessage());
                    }
                }
            )
            .collect(Collectors.toList());
    }


}


