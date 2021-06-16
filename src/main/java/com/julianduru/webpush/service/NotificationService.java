package com.julianduru.webpush.service;


import com.julianduru.util.stream.PullStreamDataRequest;
import com.julianduru.webpush.api.dto.NotificationDTO;
import org.springframework.data.domain.Page;

/**
 * created by julian
 */
public interface NotificationService {


    Page<NotificationDTO> fetchNotifications(String userId, PullStreamDataRequest dataRequest);


}
