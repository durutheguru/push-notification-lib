package com.julianduru.webpush.send.sse;

import com.julianduru.webpush.send.api.Message;
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


    // mapping of token to sinks
    private MultiValueMap<String, Sinks.Many<Message<?>>> tokenEmitterMap;


    public UserIDEmittersContainer() {
        tokenEmitterMap = new LinkedMultiValueMap<>();
    }


    public Flux<Message<?>> add(String token) {
        Sinks.Many<Message<?>> sink = Sinks.many().multicast()
            .onBackpressureBuffer();

        tokenEmitterMap.add(token, sink);

        sink.tryEmitNext(
            Message.builder()
                .messageType(Message.Type.TEXT)
                .data("Connection Established..")
                .build()
        );
        sink.tryEmitNext(
            Message.builder()
                .messageType(Message.Type.TEXT)
                .data("Waiting for Events..")
                .build()
        );

        return sink.asFlux();
    }


    public List<Sinks.Many<Message<?>>> allEmitters() {
        var list = new ArrayList<Sinks.Many<Message<?>>>();
        tokenEmitterMap.values().forEach(pair -> list.addAll(pair.stream().toList()));
        return list;
    }


}

