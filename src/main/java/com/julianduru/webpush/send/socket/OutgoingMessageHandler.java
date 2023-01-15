package com.julianduru.webpush.send.socket;

import com.julianduru.webpush.send.api.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.nio.ByteBuffer;

/**
 * created by julian on 15/01/2023
 */
@Slf4j
@RequiredArgsConstructor
public class OutgoingMessageHandler {


    private final WebSocketSession session;


    public WebSocketMessage process(Message<?> msg) {
        log.debug("Received Message from publisher: {}", msg);

        switch (msg.getMessageType()) {
            case TEXT -> {
                return session.textMessage(msg.getData().toString());
            }
            case BINARY -> {
                return session.binaryMessage(
                    dataBufferFactory -> dataBufferFactory.wrap((ByteBuffer) msg.getData())
                );
            }
            default -> {
                return session.textMessage("..");
            }
        }
    }


}
