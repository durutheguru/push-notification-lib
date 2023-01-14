package com.julianduru.webpush.send.socket;

import com.julianduru.util.JSONUtil;
import com.julianduru.webpush.send.NotificationDispatcher;
import com.julianduru.webpush.send.api.Message;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * created by julian on 12/01/2023
 */
@Component
@RequiredArgsConstructor
public class WebSocketNotificationDispatcher implements NotificationDispatcher {


    private final WebSocketHandlerImpl webSocketHandler;


    @Override
    public List<OperationStatus<String>> sendNotification(PushNotification notification) {
        return webSocketHandler.send(
            notification.getUserId(),
            Message.builder()
                .messageType(Message.Type.STRING)
                .data(JSONUtil.asJsonString(notification, ""))
                .build()
        );
    }


}
