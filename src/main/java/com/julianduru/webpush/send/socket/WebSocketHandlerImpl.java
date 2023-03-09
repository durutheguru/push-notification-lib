package com.julianduru.webpush.send.socket;

import com.julianduru.webpush.send.api.Message;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.sse.UserIDEmittersContainer;
import com.julianduru.webpush.service.auth.UserAuthenticationSource;
import com.julianduru.webpush.setup.PushRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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


    @Value("${code.config.push-notification.user.authentication-source:url}")
    private String authenticationSourceType;


    private final List<UserAuthenticationSource> authenticationSources;


    private final PushRegistry pushRegistry;


    private final MessageCommandWriter writer;


    // mapping of userId to emitters for userId
    private final ConcurrentHashMap<String, UserIDEmittersContainer> socketEmitterMap = new ConcurrentHashMap<>();



    @Override
    public Mono<Void> handle(WebSocketSession session) {
        var incomingMessageHandler = new IncomingMessageHandler(session, writer);
        var outgoingMessageHandler = new OutgoingMessageHandler(session);

        return session.send(
            getAuthenticationSource()
                .fetchNotificationToken(session)
                .flux()
                .flatMap(
                    token -> {
                        pushRegistry.addUserToNodeMapping(token.userId());
                        return addMapping(token.userId(), token.token());
                    }
                )
                .doOnError(e -> log.error("Unable to authenticate user and create Message Source", e))
                .map(outgoingMessageHandler::process)
        ).and(
            session.receive()
                .doOnNext(incomingMessageHandler::handleMessage)
        );
    }


    private Flux<Message<?>> addMapping(String authUserId, String token) {
        if (!socketEmitterMap.containsKey(authUserId)) {
            this.socketEmitterMap.put(authUserId, new UserIDEmittersContainer());
        }

        return this.socketEmitterMap.get(authUserId).add(token);
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


    private UserAuthenticationSource getAuthenticationSource() {
        return authenticationSources
            .stream()
            .filter(
                source -> source.supports(authenticationSourceType)
            )
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No Authentication Source found for type " + authenticationSourceType)
            );
    }


}

