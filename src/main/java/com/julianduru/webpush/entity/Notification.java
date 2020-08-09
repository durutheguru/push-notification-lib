package com.julianduru.webpush.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.julianduru.security.entity.BaseEntity;
import com.julianduru.util.jpa.CryptoConverter;
import com.julianduru.util.json.ZonedDateTimeDeserializer;
import com.julianduru.util.json.ZonedDateTimeSerializer;
import com.julianduru.webpush.NotificationConstant;
import com.julianduru.webpush.event.NotificationEvent;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by julian
 */
@Data
@Entity
@Table(name = NotificationConstant.TABLE_PREFIX + "notification")
public class Notification extends BaseEntity {


    @Column(nullable = false, columnDefinition = "TEXT")
    @NotEmpty(message = "Notification Message cannot be empty")
    @Convert(converter = CryptoConverter.class)
    private String message;


    @Column(nullable = false, length = 100)
    @NotEmpty(message = "User ID cannot be empty")
    private String userId;


    @Column(nullable = false)
    private boolean received;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime receivedTimeStamp;


    @Column(nullable = false)
    private boolean viewed;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime viewedTimeStamp;


    public static List<Notification> listFrom(NotificationEvent event) {
        if (event.getUserIds() == null || event.getUserIds().isEmpty()) {
            return Collections.emptyList();
        }

        return event.getUserIds().stream().map(id -> {
            Notification notification = new Notification();

            notification.setUserId(id);
            notification.setMessage(event.getMessage());

            return notification;
        }).collect(Collectors.toList());
    }


    public Notification received(boolean received) {
        if (isReceived()) {
            return this;
        }

        setReceived(received);
        setReceivedTimeStamp(isReceived() ? ZonedDateTime.now() : null);

        return this;
    }


}

