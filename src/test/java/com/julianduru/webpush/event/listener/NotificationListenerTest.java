package com.julianduru.webpush.event.listener;


import com.google.common.collect.Sets;
import com.julianduru.security.Auth;
import com.julianduru.util.test.JpaDataProvider;
import com.julianduru.webpush.BaseServiceIntegrationTest;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.event.NotificationEventTestHelper;
import com.julianduru.webpush.send.sse.Emitters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    private NotificationEventTestHelper notificationEventTestHelper;


    @Autowired
    private JpaDataProvider<NotificationSubscription> subscriptionDataProvider;



    @Test
    public void testFiringNotificationEvent() throws Exception {
        String username = Auth.getUserAuthId(true).authUsername;

        NotificationEvent event = new NotificationEvent(
            Sets.newHashSet(username),
            "Notification Event " + System.currentTimeMillis()
        );

        eventPublisher.publishEvent(event);

        notificationEventTestHelper.testPersistedNotificationFromEvent(event);
    }


    @Test
    public void testNotificationIsReceivedOnWebPushToSubscription() throws Exception {
        String username = Auth.getUserAuthId(true).authUsername;

        NotificationSubscription sample = new NotificationSubscription();
        sample.setUserId(username);

        subscriptionDataProvider.save(sample);

        notificationEventTestHelper.testPublishedEventToUsername(username);
    }


    @Test
    public void testNotificationIsReceivedOnSseSubscription() throws Exception {
        String username = Auth.getUserAuthId(true).authUsername;

        emitters.add(new SseEmitter());

        notificationEventTestHelper.testPublishedEventToUsername(username);
    }



}
