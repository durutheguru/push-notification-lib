package com.julianduru.webpush.service;


import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.api.PushNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * created by julian
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final PushNotificationRepository notificationRepository;


    @Override
    public Page<PushNotification> fetchNotifications(String userId, Pageable pageable) {
        return notificationRepository.fetchNotifications(userId, pageable);
    }


}
