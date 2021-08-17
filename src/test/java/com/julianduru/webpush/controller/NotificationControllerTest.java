package com.julianduru.webpush.controller;


import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.data.NotificationDataProvider;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.rest.BaseRestIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * created by julian
 */
@ActiveProfiles({
    "localtest"
})
@SpringBootTest(
    classes = {
        NotificationAutoConfiguration.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class NotificationControllerTest extends BaseRestIntegrationTest {


    @Autowired
    private NotificationDataProvider notificationDataProvider;


    @Test
    public void testFetchingUserNotifications() throws Exception {
        var username = notificationDataProvider
            .getFaker()
            .internet()
            .emailAddress();

        var notifSample = new Notification();
        notifSample.setUserId(username);

        notificationDataProvider.save(notifSample, 4);

        mockMvc.perform(
            get(NotificationController.PATH + "?userId=" + username)
        ).andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    public void testInitializingUserNotifications() throws Exception {
        var username = "durutheguru@gmail.com";

        var notifSample = new Notification();
        notifSample.setUserId(username);

        notificationDataProvider.save(notifSample, 4);
    }


}
