package com.julianduru.webpush.send.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.julianduru.util.json.ZonedDateTimeDeserializer;
import com.julianduru.util.json.ZonedDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * created by julian on 29/11/2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushNotification {


    private String uuid;


    private String userId;


    private String message;


    private boolean received;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime receivedTimeStamp;


    private boolean viewed;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime viewedTimeStamp;



}
