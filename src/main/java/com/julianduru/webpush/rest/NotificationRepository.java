package com.julianduru.webpush.rest;


import com.julianduru.webpush.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

/**
 * created by julian
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByUserIdAndMessage(String userId, String message);


    Page<Notification> findByUserId(String userId, Pageable pageable);


    @Query(
        "SELECT n FROM Notification n WHERE n.userId = :userId AND n.timeAdded >= :afterTime " +
        "AND n.timeAdded <= :beforeTime"
    )
    Page<Notification> findByUserIdAndTimeAddedBetween(
        @Param("userId") String userId,
        @Param("afterTime") ZonedDateTime afterTime,
        @Param("beforeTime") ZonedDateTime beforeTime,
        Pageable pageable
    );


    @Query(
        "SELECT n FROM Notification n WHERE n.userId = :userId AND n.timeAdded <= :beforeTime"
    )
    Page<Notification> findByUserIdAndTimeAddedBefore(
        @Param("userId") String userId,
        @Param("beforeTime") ZonedDateTime beforeTime,
        Pageable pageable
    );


    @Query(
        "SELECT n FROM Notification n WHERE n.userId = :userId AND n.timeAdded >= :afterTime"
    )
    Page<Notification> findByUserIdAndTimeAddedAfter(
        @Param("userId") String userId,
        @Param("afterTime") ZonedDateTime afterTime,
        Pageable pageable
    );



    List<Notification> findByUserIdInAndMessage(Collection<String> userIds, String message);


}
