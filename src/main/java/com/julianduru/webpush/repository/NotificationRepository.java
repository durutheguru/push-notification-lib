package com.julianduru.webpush.repository;


import com.julianduru.webpush.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * created by julian
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {



}
