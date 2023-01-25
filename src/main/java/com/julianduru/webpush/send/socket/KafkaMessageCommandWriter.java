package com.julianduru.webpush.send.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * created by julian on 24/01/2023
 */
@Component
@RequiredArgsConstructor
public class KafkaMessageCommandWriter {


    @Value("${code.config.kafka.user-command.topic-name}")
    private String userCommandTopicName;


    private final KafkaTemplate<String, String> userCommandKafkaTemplate;



    public void sendMessage(String key, String message) {
        userCommandKafkaTemplate.send(
            userCommandTopicName, key, message
        );
    }


    public void sendMessage(String message) {
        userCommandKafkaTemplate.send(
            userCommandTopicName, generateKey(), message
        );
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
