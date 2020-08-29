package com.julianduru.webpush.event;


import com.google.common.collect.Sets;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.rest.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian
 */
@Component
@RequiredArgsConstructor
public class NotificationEventTestHelper {


    private final ApplicationEventPublisher eventPublisher;


    private final NotificationRepository notificationRepository;



    public void  testPublishedEventToUsername(String username) throws Exception {
        NotificationEvent event = new NotificationEvent(
            Sets.newHashSet(username),
            "Sample Message Notification " + System.currentTimeMillis()
        );

        eventPublisher.publishEvent(event);

        Notification check = new Notification();
        check.setReceived(true);
        check.setUserId(username);

        testPersistedNotificationFromEvent(event, check);
    }


    public void testPersistedNotificationFromEvent(NotificationEvent event) throws Exception {
        List<Notification> notifications = notificationRepository
            .findByUserIdInAndMessage(event.getUserIds(), event.getMessage());

        assertThat(notifications).isNotEmpty();
    }


    public void testPersistedNotificationFromEvent(NotificationEvent event, Notification check) throws Exception {
        List<Notification> notifications = notificationRepository
            .findByUserIdInAndMessage(event.getUserIds(), event.getMessage());

        assertThat(notifications).isNotEmpty();
        assertThat(notifications.get(0))
            .isEqualToComparingOnlyGivenFields(check, "userId", "received");
    }


}
