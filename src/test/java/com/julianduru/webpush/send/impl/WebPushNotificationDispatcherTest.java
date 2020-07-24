package com.julianduru.webpush.send.impl;


import com.julianduru.security.Auth;
import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.config.TestBeansConfig;
import com.julianduru.webpush.config.TestConfig;
import com.julianduru.webpush.data.DataProvider;
import com.julianduru.webpush.data.NotificationDataProvider;
import com.julianduru.webpush.data.NotificationSubscriptionDataProvider;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.entity.NotificationSubscription;
import com.julianduru.webpush.send.util.HttpResponseListAssert;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * created by julian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = {
        TestConfig.class,
        TestBeansConfig.class,
        NotificationDataProvider.class,
        NotificationSubscriptionDataProvider.class,
        WebPushNotificationDispatcher.class,
        NotificationAutoConfiguration.class,
    }
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
@TestPropertySource(
    properties = {
        "test.code.config.mock.push-service-successful=true"
    }
)
public class WebPushNotificationDispatcherTest {


    @Autowired
    WebPushNotificationDispatcher dispatcher;


    @Autowired
    DataProvider<Notification> dataProvider;


    @Autowired
    DataProvider<NotificationSubscription> subscriptionDataProvider;



    @Before
    public void before() {
        NotificationSubscription sample = new NotificationSubscription();
        sample.setUserId(Auth.getUserAuthId(true).authUsername);

        subscriptionDataProvider.save(sample, 2);
    }



    @Test
    public void testSendingNotification() throws Exception {
        Notification sample = new Notification();
        sample.setUserId(Auth.getUserAuthId(true).authUsername);

        Notification notification = dataProvider.provide(sample);

        List<HttpResponse> responseList = dispatcher.sendNotification(notification).get();

        HttpResponseListAssert.checkList(responseList);
    }



}
