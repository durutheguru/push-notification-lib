package com.julianduru.webpush.send.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Collections;

/**
 * created by julian on 10/01/2023
 */
@Slf4j
@Configuration
public class WebSocketConfiguration {


    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }


    @Bean
    public HandlerMapping handlerMapping(WebSocketHandlerImpl webSocketHandler) {
        return new SimpleUrlHandlerMapping(
            Collections.singletonMap("/ws/connect", webSocketHandler), 1
        );
    }


}


