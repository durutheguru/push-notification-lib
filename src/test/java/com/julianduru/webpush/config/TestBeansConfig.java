package com.julianduru.webpush.config;


import com.julianduru.webpush.send.NotificationDispatcher;
import nl.martijndwars.webpush.PushService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.any;

/**
 * created by julian
 */
@TestConfiguration
public class TestBeansConfig {


    @Bean
    @Primary
    @ConditionalOnProperty(
        prefix = "test.code.config.mock", name = "push-service-successful", havingValue = "true"
    )
    public PushService mockPushService() throws Exception {
        PushService pushService = Mockito.mock(PushService.class);

        Mockito.when(pushService.send(any())).thenAnswer(
            i -> NotificationDispatcher.defaultSuccessNotifResponse("Sent Notification")
        );

        return pushService;
    }


}
