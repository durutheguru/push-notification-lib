package com.julianduru.webpush.kafka;

import com.julianduru.webpush.send.NotificationDispatchGateway;
import com.julianduru.webpush.send.api.PushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * created by julian on 29/11/2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMessageConsumer {


    private final NotificationDispatchGateway dispatchGateway;


    @KafkaListener(
        topics = {"user-push-notification"},
        groupId = "kafkaUserNotificationsListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, String> record) {
        log.info("User Notification Consumer Record: {}", record);

        var value = record.value();
        var values = value.split("\\|");
        if (values.length != 3) {
            throw new IllegalArgumentException("Cannot process Message Value " + value);
        }

        var userId = values[0];
        var messageType = values[1];
        var message = values[2];

        dispatchGateway.dispatch(
            List.of(
                PushNotification.builder()
                    .uuid(UUID.randomUUID().toString())
                    .userId(userId)
                    .message(message)
                    .type(messageType)
                    .build()
            )
        );
    }


}

