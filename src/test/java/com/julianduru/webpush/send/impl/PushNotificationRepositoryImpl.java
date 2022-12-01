package com.julianduru.webpush.send.impl;

import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * created by julian on 29/11/2022
 */
@Component
public class PushNotificationRepositoryImpl implements PushNotificationRepository {


    private final List<PushNotification> notifications = new ArrayList<>();


    @Override
    public void save(PushNotification notification) {
        notifications.add(notification);
    }


    @Override
    public Page<PushNotification> fetchNotifications(String username, Pageable pageable) {
        var list = notifications.stream()
            .filter(n -> n.getUserId().equalsIgnoreCase(username))
            .toList();

        return new PageImpl<>(list, pageable, list.size());
    }


}
