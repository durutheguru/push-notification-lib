package com.julianduru.webpush.send.socket;

import com.julianduru.queueintegrationlib.module.publish.OutgoingMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * created by julian on 24/01/2023
 */
@Component
@RequiredArgsConstructor
public class MessageCommandWriter {


    @Value("${code.config.kafka.user-command.topic-name}")
    private String userCommandTopicName;


    private final OutgoingMessagePublisher messagePublisher;



    public void sendMessage(String message) {
        messagePublisher.publish(userCommandTopicName, message, true);
    }


    private String generateKey() {
        return String.format(
            "%s-%d-%s",
            UUID.randomUUID(),
            System.currentTimeMillis(),
            UUID.randomUUID()
        );
    }


}


