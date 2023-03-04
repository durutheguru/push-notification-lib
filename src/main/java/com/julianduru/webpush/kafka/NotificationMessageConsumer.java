package com.julianduru.webpush.kafka;

import com.julianduru.webpush.annotation.PushServer;
import com.julianduru.webpush.send.NotificationDispatchGateway;
import com.julianduru.webpush.send.api.PushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * created by julian on 29/11/2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMessageConsumer {


    private final NotificationDispatchGateway dispatchGateway;


    @PushServer
    public void onMessage(String reference, Map<String, String> header, String payload) {
        log.info(
            "Kafka: GroupID: {}. Notification Received: {} - {}",
            KafkaUtils.getConsumerGroupId(), reference, payload
        );

        dispatchGateway.dispatch(
            List.of(
                PushNotification.builder()
                    .uuid(UUID.randomUUID().toString())
                    .type(header.get("NotificationType"))
                    .userId(header.get("UserID"))
                    .message(payload)
                    .build()
            )
        );
    }


}

