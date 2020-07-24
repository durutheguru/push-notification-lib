package com.julianduru.webpush.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.julianduru.security.entity.BaseEntity;
import com.julianduru.util.jpa.CryptoConverter;
import com.julianduru.util.json.ZonedDateTimeDeserializer;
import com.julianduru.util.json.ZonedDateTimeSerializer;
import com.julianduru.webpush.NotificationConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * created by julian
 */
@Data
@Entity
@Table(name = NotificationConstant.TABLE_PREFIX + "notification_subscription")
public class NotificationSubscription extends BaseEntity {


    @Column(nullable = false, columnDefinition = "TEXT")
    @NotEmpty(message = "Endpoint cannot be empty")
    private String endpoint;


    @Column(nullable = false)
    @NotEmpty(message = "Public Key cannot be empty")
    @Convert(converter = CryptoConverter.class)
    private String publicKey;


    @Column(nullable = false)
    @NotEmpty(message = "Auth Token cannot be empty")
    @Convert(converter = CryptoConverter.class)
    private String authToken;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime expirationTime;


    @Column(nullable = false, length = 100)
    private String userId;


}
