package com.julianduru.webpush.controller;


import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.NotificationConstant;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.data.PushNotificationDataProvider;
import com.julianduru.webpush.rest.BaseRestIntegrationTest;
import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.api.PushNotification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * created by julian
 */
@SpringBootTest(
    classes = {
        NotificationAutoConfiguration.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class PushNotificationControllerTest extends BaseRestIntegrationTest {


    @Autowired
    private PushNotificationDataProvider notificationDataProvider;


    @Autowired
    private PushNotificationRepository notificationRepository;


    @Test
    public void testFetchingUserNotifications() throws Exception {
        var username = notificationDataProvider
            .getFaker()
            .internet()
            .emailAddress();

        var notifSample = new PushNotification();
        notifSample.setUserId(username);

        var notifications = notificationDataProvider.provide(notifSample, 4);
        notifications.forEach(notificationRepository::save);

        mockMvc.perform(
            get(NotificationConstant.SSE_API_PREFIX + "/notifications" + "?userId=" + username)
        ).andDo(print())
            .andExpect(status().isOk());
    }



}
