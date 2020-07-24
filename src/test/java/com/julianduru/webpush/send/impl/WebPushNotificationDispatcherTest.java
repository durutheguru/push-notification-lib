package com.julianduru.webpush.send.impl;


import com.julianduru.security.Auth;
import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.config.TestConfig;
import com.julianduru.webpush.data.DataProvider;
import com.julianduru.webpush.data.NotificationDataProvider;
import com.julianduru.webpush.data.NotificationSubscriptionDataProvider;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.entity.NotificationSubscription;
import com.julianduru.webpush.repository.NotificationSubscriptionRepository;
import com.julianduru.webpush.send.NotificationDispatcher;
import com.julianduru.webpush.send.util.HttpResponseListAssert;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * created by julian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = {
        TestConfig.class,
        NotificationDataProvider.class,
        NotificationSubscriptionDataProvider.class,
        WebPushNotificationDispatcher.class,
        NotificationAutoConfiguration.class,
    }
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
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


    @TestConfiguration
    static class BeansConfig {


        @Bean
        @Primary
        public PushService mockPushService() throws Exception {
            PushService pushService = Mockito.mock(PushService.class);

            Mockito.when(pushService.send(any())).thenAnswer(
                i -> NotificationDispatcher.defaultSuccessNotifResponse("Sent Notification")
            );

            return pushService;
        }


    }


}
