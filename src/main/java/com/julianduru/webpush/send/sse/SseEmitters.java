package com.julianduru.webpush.send.sse;


import com.julianduru.security.Auth;
import com.julianduru.security.entity.UserAuthId;
import com.julianduru.util.JSONUtil;
import com.julianduru.webpush.exception.ServerSentEventException;
import com.julianduru.webpush.send.api.OperationStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * created by julian
 */
@Component
public class SseEmitters implements Emitters {


    private ConcurrentHashMap<String, List<SseEmitter>> sseEmitterMap = new ConcurrentHashMap<>();


    public Map<String, List<SseEmitter>> getEmitterMap() {
        return Collections.unmodifiableMap(sseEmitterMap);
    }


    public SseEmitter add(SseEmitter emitter) throws IllegalStateException {
        List<SseEmitter> emitterList = addMapping(
            Auth.getUserAuthId(true).authUsername, emitter
        );

        emitter.onCompletion(() -> emitterList.remove(emitter));
        emitter.onTimeout(() -> emitterList.remove(emitter));

        return emitter;
    }


    public List<OperationStatus<String>> send(String authUserId, Object obj) {
        List<SseEmitter> emitterList = getEmitters(authUserId);
        List<SseEmitter> failedEmitters = new ArrayList<>();

        var responseList = emitterList
            .stream()
            .map(
                emitter -> {
                    try {
                        emitter.send(
                            SseEmitter.event()
                                .name(authUserId)
                                .data(JSONUtil.asJsonString(obj), MediaType.APPLICATION_JSON)
                        );
                        return OperationStatus.success("Sent Server Event");
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                        failedEmitters.add(emitter);
                        return OperationStatus.failure(e.getMessage());
                    }
                }
            )
            .collect(Collectors.toList());

        emitterList.removeAll(failedEmitters);

        return responseList;
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


    private List<SseEmitter> addMapping(String authUserId, SseEmitter emitter) {
        final List<SseEmitter> emitterList = getEmitters(authUserId);

        emitterList.add(emitter);

        this.sseEmitterMap.put(authUserId, emitterList);
        return emitterList;
    }


    private List<SseEmitter> getEmitters(String authUserId) {
        List<SseEmitter> emitterList = sseEmitterMap.get(authUserId);

        return emitterList != null ?
             emitterList : Collections.synchronizedList(new ArrayList<>());
    }


}
