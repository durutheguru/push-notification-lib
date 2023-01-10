package com.julianduru.webpush.send;


import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;

import java.util.List;

/**
 * created by julian
 */
public interface NotificationDispatcher {


    List<OperationStatus<String>> sendNotification(PushNotification notification) ;


}

