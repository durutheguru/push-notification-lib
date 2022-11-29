package com.julianduru.webpush.send;


import com.julianduru.webpush.send.api.PushNotification;

import java.util.List;

/**
 * created by julian
 */
public interface NotificationDispatchGateway {


    void dispatch(List<PushNotification> notifications);


}
