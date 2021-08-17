package com.julianduru.webpush.api.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.julianduru.util.json.ZonedDateTimeDeserializer;
import com.julianduru.util.json.ZonedDateTimeSerializer;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * created by julian
 */
@Data
public class NotificationDTO extends BaseDTO {


    private String message;


    private String userId;


    private boolean received;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime receivedTimeStamp;


    private boolean viewed;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime viewedTimeStamp;


}


