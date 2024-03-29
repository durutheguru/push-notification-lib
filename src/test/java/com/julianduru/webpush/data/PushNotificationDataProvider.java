package com.julianduru.webpush.data;

import com.julianduru.util.test.DataProvider;
import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * created by julian on 29/11/2022
 */
@Component
public class PushNotificationDataProvider implements DataProvider<PushNotification> {

    @Override
    public PushNotification provide() {
        return PushNotification.builder()
            .uuid(UUID.randomUUID().toString())
            .userId(faker.code().isbn10())
            .message(faker.lorem().sentence())
            .type(faker.lorem().word())
            .build();
    }


}
