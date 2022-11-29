package com.julianduru.webpush.service;


import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * created by julian
 */
public interface NotificationService {


    Page<PushNotification> fetchNotifications(String userId, Pageable pageable);


}
