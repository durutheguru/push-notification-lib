package com.julianduru.webpush.send;

import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * created by julian on 29/11/2022
 */
public interface PushNotificationRepository {


    void save(PushNotification notification);


    Page<PushNotification> fetchNotifications(String username, Pageable pageable);


}

