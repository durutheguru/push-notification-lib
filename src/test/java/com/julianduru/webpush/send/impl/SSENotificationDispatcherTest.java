package com.julianduru.webpush.send.impl;


import com.julianduru.security.Auth;
import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.config.TestConfig;
import com.julianduru.webpush.data.PushNotificationDataProvider;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.sse.SseEmitters;
import com.julianduru.webpush.send.util.HttpResponseListAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

/**
 * created by julian
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {
    TestConfig.class,
    SseEmitters.class,
    SSENotificationDispatcher.class,
    NotificationAutoConfiguration.class,
})
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class SSENotificationDispatcherTest {


    @Autowired
    SseEmitters emitters;


    @Autowired
    SSENotificationDispatcher dispatcher;


    @Autowired
    PushNotificationDataProvider dataProvider;


    @Test
    @Disabled
    public void testSendingNotification() throws Exception {
        emitters.add(TestConstants.TEST_USER_NAME, UUID.randomUUID().toString());

        var sample = new PushNotification();
        sample.setUserId(Auth.getUserAuthId(true).authUsername);

        var notification = dataProvider.provide(sample);

        var responseList = dispatcher.sendNotification(notification);
        HttpResponseListAssert.checkList(responseList);
    }


}

