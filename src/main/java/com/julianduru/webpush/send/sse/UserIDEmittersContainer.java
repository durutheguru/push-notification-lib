package com.julianduru.webpush.send.sse;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

/**
 * created by julian on 01/12/2022
 */
@Slf4j
@Data
public class UserIDEmittersContainer {

    static Long sseTimeout = 18000000L;


    private MultiValueMap<String, Sinks.Many<Object>> tokenEmitterMap;


    public UserIDEmittersContainer() {
        tokenEmitterMap = new LinkedMultiValueMap<>();
    }


    public Flux<Object> add(String token) {
        var sink = Sinks.many().multicast()
            .onBackpressureBuffer();

        tokenEmitterMap.add(token, sink);

        sink.tryEmitNext("Connection Established..");
        sink.tryEmitNext("Waiting for Events..");

        return sink.asFlux();
    }


    public List<Sinks.Many<Object>> allEmitters() {
        var list = new ArrayList<Sinks.Many<Object>>();
        tokenEmitterMap.values().forEach(pair -> list.addAll(pair.stream().toList()));
        return list;
    }


}

