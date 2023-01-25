package com.julianduru.webpush.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * created by julian on 24/01/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {


    private final KafkaProperties kafkaProperties;


    @Value("${code.config.kafka.user-command.topic-name}")
    private String userCommandTopicName;


    @Bean
    public Map<String, Object> userCommandProducerConfigs() {
        var props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        return props;
    }


    @Bean
    public ProducerFactory<String, String> userCommandProducerFactory() {
        return new DefaultKafkaProducerFactory<>(userCommandProducerConfigs());
    }


    @Bean
    public KafkaTemplate<String, String> userCommandKafkaTemplate() {
        return new KafkaTemplate<>(userCommandProducerFactory());
    }


    @Bean
    public NewTopic userCommandTopic() {
        return new NewTopic(userCommandTopicName, 3, (short) 1);
    }


}


