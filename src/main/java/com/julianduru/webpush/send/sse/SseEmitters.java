package com.julianduru.webpush.send.sse;


import com.julianduru.security.Auth;
import com.julianduru.security.entity.UserAuthId;
import com.julianduru.util.JSONUtil;
import com.julianduru.webpush.exception.ServerSentEventException;
import com.julianduru.webpush.send.api.Message;
import com.julianduru.webpush.send.api.OperationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * created by julian
 */
@Slf4j
@Component
public class SseEmitters implements Emitters {


    private ConcurrentHashMap<String, UserIDEmittersContainer> sseEmitterMap = new ConcurrentHashMap<>();


    public Map<String, UserIDEmittersContainer> getEmitterMap() {
        return Collections.unmodifiableMap(sseEmitterMap);
    }


    public Flux<Message<?>> add(String userId, String token) throws IllegalStateException {
        return addMapping(userId, token);
    }


    public List<OperationStatus<String>> send(String authUserId, Object obj) {
        var emittersContainer = sseEmitterMap.get(authUserId);
        if (emittersContainer == null) {
            log.info("No Emitters for userId {}", authUserId);
            return List.of(OperationStatus.failure());
        }

        var sinkList = emittersContainer.allEmitters();

        return sinkList
            .stream()
            .map(
                sink -> {
                    try {
                        if (sink.currentSubscriberCount() < 1) {
                            throw new IllegalStateException("No subscribers on sink. To be scheduled for removal");
                        }
                        sink.tryEmitNext(
                            Message.builder()
                                .messageType(Message.Type.TEXT)
                                .data(JSONUtil.asJsonString(obj))
                                .build()
                        );
                        return OperationStatus.success("Sent Server Event");
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


    public List<OperationStatus<String>> send(Object obj) throws ServerSentEventException {
        Optional<UserAuthId> authId = Auth.getUserAuthId();
        if (authId.isPresent()) {
            return send(authId.get().authUsername, obj);
        }
        else {
            throw new ServerSentEventException("No Authenticated User");
        }
    }


    private Flux<Message<?>> addMapping(String authUserId, String token) {
        if (!sseEmitterMap.containsKey(authUserId)) {
            this.sseEmitterMap.put(authUserId, new UserIDEmittersContainer());
        }

        return this.sseEmitterMap.get(authUserId).add(token);
    }


}
