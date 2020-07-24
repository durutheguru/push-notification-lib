package com.julianduru.webpush.event.listener;


import com.julianduru.security.Auth;
import com.julianduru.webpush.BaseServiceIntegrationTest;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.data.DataProvider;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.entity.NotificationSubscription;
import com.julianduru.webpush.event.NotificationEvent;
import com.julianduru.webpush.rest.NotificationRepository;
import com.julianduru.webpush.send.sse.Emitters;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian
 */
@WithMockUser(username = TestConstants.TEST_USER_NAME)
@TestPropertySource(
    properties = {
        "test.code.config.mock.push-service-successful=true"
    }
)
public class NotificationListenerTest extends BaseServiceIntegrationTest {


    @Autowired
    private Emitters emitters;


    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Autowired
    private NotificationRepository notificationRepository;


    @Autowired
    private DataProvider<NotificationSubscription> subscriptionDataProvider;



    @Test
    public void testFiringNotificationEvent() throws Exception {
        String username = Auth.getUserAuthId(true).authUsername;

        NotificationEvent event = new NotificationEvent(username, "Notification Event " + System.currentTimeMillis());

        eventPublisher.publishEvent(event);

        testPersistedNotificationFromEvent(event);
    }


    @Test
    public void testNotificationIsReceivedOnWebPushToSubscription() throws Exception {
        String username = Auth.getUserAuthId(true).authUsername;

        NotificationSubscription sample = new NotificationSubscription();
        sample.setUserId(username);

        subscriptionDataProvider.save(sample);

        testPublishedEventToUsername(username);
    }


    @Test
    public void testNotificationIsReceivedOnSseSubscription() throws Exception {
        String username = Auth.getUserAuthId(true).authUsername;

        emitters.add(new SseEmitter());

        testPublishedEventToUsername(username);
    }


    private void  testPublishedEventToUsername(String username) throws Exception {
        NotificationEvent event = new NotificationEvent(
            username, "Sample Message Notification" + System.currentTimeMillis());

        eventPublisher.publishEvent(event);


        Notification check = new Notification();
        check.setReceived(true);
        check.setUserId(username);

        testPersistedNotificationFromEvent(event, check);
    }



    private void testPersistedNotificationFromEvent(NotificationEvent event) throws Exception {
        List<Notification> notifications = notificationRepository
            .findByUserIdAndMessage(event.getUserId(), event.getMessage());

        assertThat(notifications).isNotEmpty();
    }


    private void testPersistedNotificationFromEvent(NotificationEvent event, Notification check) throws Exception {
        List<Notification> notifications = notificationRepository
            .findByUserIdAndMessage(event.getUserId(), event.getMessage());

        assertThat(notifications).isNotEmpty();
        assertThat(notifications.get(0))
            .isEqualToComparingOnlyGivenFields(check, "userId", "received");
    }


}
