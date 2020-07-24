package com.julianduru.webpush.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {


    private String userId;


    private String message;


}
