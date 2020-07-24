package com.julianduru.webpush.rest;


import com.julianduru.webpush.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by julian
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByUserIdAndMessage(String userId, String message);



}
