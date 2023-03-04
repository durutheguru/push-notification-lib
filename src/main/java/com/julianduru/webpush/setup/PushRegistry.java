package com.julianduru.webpush.setup;

import com.julianduru.queueintegrationlib.config.QueueIntegrationKafkaProducerConfig;
import com.julianduru.queueintegrationlib.module.subscribe.DynamicConsumerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by Julian Duru on 04/03/2023
 */
@Component
@RequiredArgsConstructor
public class PushRegistry {


    private final QueueIntegrationKafkaProducerConfig queueIntegrationKafkaProducerConfig;

    private final DynamicConsumerFactory consumerFactory;

    private static String NODE_ID;

    private static AtomicBoolean initialized = new AtomicBoolean(false);


    public static String getNodeId() {
        return NODE_ID;
    }


    protected void initializeNode(String nodeId) {
        try {
            if (initialized.get()) {
                throw new IllegalStateException("Attempting to initialize an already initialized node.");
            }

            NODE_ID = nodeId;
            queueIntegrationKafkaProducerConfig.createTopics(nodeId);
            consumerFactory.createConsumer(nodeId);
            initialized.set(true);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }


}


