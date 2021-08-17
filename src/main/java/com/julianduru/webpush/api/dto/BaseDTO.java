package com.julianduru.webpush.api.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.julianduru.util.entity.TimeStampAware;
import com.julianduru.util.jpa.ZonedDateTimeConverter;
import com.julianduru.util.json.ZonedDateTimeDeserializer;
import com.julianduru.util.json.ZonedDateTimeSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.time.ZonedDateTime;

/**
 * created by julian
 */
@Data
public class BaseDTO implements TimeStampAware {


    private Long id;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime timeAdded;


    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime timeUpdated;


}

