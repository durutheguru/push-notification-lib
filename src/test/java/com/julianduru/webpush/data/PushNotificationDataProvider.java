package com.julianduru.webpush.data;

import com.julianduru.util.test.DataProvider;
import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.stereotype.Component;

/**
 * created by julian on 29/11/2022
 */
@Component
public class PushNotificationDataProvider implements DataProvider<PushNotification> {

    @Override
    public PushNotification provide() {
        return PushNotification.builder()
            .userId(faker.code().isbn10())
            .message(faker.lorem().sentence())
            .build();
    }


}
