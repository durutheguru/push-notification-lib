package com.julianduru.webpush.event;


import com.julianduru.webpush.entity.Notification;

import java.util.List;

/**
 * created by julian
 */
public interface NotificationDispatchGateway {


    void dispatch(List<Notification> notifications);


}
