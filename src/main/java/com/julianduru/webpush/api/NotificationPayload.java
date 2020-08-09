package com.julianduru.webpush.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPayload<T> {


    String type;


    T data;


    String message;


}
