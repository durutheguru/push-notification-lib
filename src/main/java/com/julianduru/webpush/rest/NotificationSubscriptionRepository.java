package com.julianduru.webpush.rest;


import com.julianduru.webpush.entity.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * created by julian
 */
@Repository
@RepositoryRestResource(path = NotificationSubscriptionRepository.PATH)
public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription, Long> {


    String PATH = "notification_subscription";


    List<NotificationSubscription> findByUserId(String userId);


    long countByUserId(String userId);


    List<NotificationSubscription> findByPublicKeyAndAuthToken(String publicKey, String authToken);


    Optional<NotificationSubscription> findByEndpoint(String endPoint);


    boolean existsByEndpoint(String endPoint);


}


