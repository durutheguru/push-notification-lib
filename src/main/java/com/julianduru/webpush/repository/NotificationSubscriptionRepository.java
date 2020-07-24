package com.julianduru.webpush.repository;


import com.julianduru.webpush.entity.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by julian
 */
@Repository
@RepositoryRestResource(path = NotificationSubscriptionRepository.PATH)
public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {


    String PATH = "notification_subscription";


    List<NotificationSubscription> findByUserId(String userId);


    long countByUserId(String userId);


}

