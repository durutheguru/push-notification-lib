package com.julianduru.webpush.send.sse;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by julian on 01/12/2022
 */
@Slf4j
@Data
public class UserIDEmittersContainer {

    static Long sseTimeout = 18000000L;


    private Map<String, SseEmitter> tokenEmitterMap;


    public UserIDEmittersContainer() {
        tokenEmitterMap = new HashMap<>();
    }


    public SseEmitter add(String token) {
        if (tokenEmitterMap.containsKey(token)) {
            return tokenEmitterMap.get(token);
        }

        var sink = Sinks.many().multicast().onBackpressureBuffer();

        var emitter = new SseEmitter(sseTimeout);

        emitter.onCompletion(() -> tokenEmitterMap.remove(token));
        emitter.onTimeout(() -> tokenEmitterMap.remove(token));

        tokenEmitterMap.put(token, emitter);

        try {
            emitter.send(
                SseEmitter.event()
                    .reconnectTime(10000000)
            );
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return emitter;
    }


    public List<SseEmitter> allEmitters() {
        return tokenEmitterMap.values().stream().toList();
    }


    public void removeEmitters(List<SseEmitter> emitters) {

    }


}

