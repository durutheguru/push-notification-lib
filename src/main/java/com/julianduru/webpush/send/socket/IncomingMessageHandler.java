package com.julianduru.webpush.send.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * created by julian on 15/01/2023
 */
@Slf4j
@RequiredArgsConstructor
public class IncomingMessageHandler {


    private final WebSocketSession session;


    public void handleMessage(WebSocketMessage message) {
        switch (message.getType()) {
            case BINARY -> handleBinaryMessage(session, message);
            case TEXT -> handleTextMessage(session, message);
        }
    }


    private void handleTextMessage(WebSocketSession session, WebSocketMessage message) {
        log.info("Text Message Received: {}", message);
    }


    private void handleBinaryMessage(WebSocketSession session, WebSocketMessage message) {
        log.info("Binary Message Received: {}", message);
    }


}
