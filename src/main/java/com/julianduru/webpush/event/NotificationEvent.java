package com.julianduru.webpush.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * created by julian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {


    private Set<String> userIds;


    private String message;


}
