package com.julianduru.webpush.send.impl;


import com.julianduru.security.Auth;
import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.config.TestConfig;
import com.julianduru.util.test.DataProvider;
import com.julianduru.webpush.data.NotificationDataProvider;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.send.sse.SseEmitters;
import com.julianduru.webpush.send.util.HttpResponseListAssert;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * created by julian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    TestConfig.class,
    SseEmitters.class,
    SSENotificationDispatcher.class,
    NotificationDataProvider.class,
    NotificationAutoConfiguration.class,
})
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class SSENotificationDispatcherTest {


    @Autowired
    SseEmitters emitters;


    @Autowired
    SSENotificationDispatcher dispatcher;


    @Autowired
    DataProvider<Notification> dataProvider;


    @Test
    public void testSendingNotification() throws Exception {
        emitters.add(new SseEmitter());

        Notification sample = new Notification();
        sample.setUserId(Auth.getUserAuthId(true).authUsername);

        Notification notification = dataProvider.provide(sample);

        List<HttpResponse> responseList = dispatcher.sendNotification(notification).get();

        HttpResponseListAssert.checkList(responseList);
    }


}
