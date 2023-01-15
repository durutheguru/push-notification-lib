package com.julianduru.webpush.send.socket;

import org.springframework.web.reactive.socket.WebSocketMessage;

/**
 * created by julian on 15/01/2023
 */
public record MessageEvent(
    WebSocketMessage message
) {
}
