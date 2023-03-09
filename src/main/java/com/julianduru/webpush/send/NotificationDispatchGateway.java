package com.julianduru.webpush.send;


import com.julianduru.queueintegrationlib.model.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;

import java.util.List;

/**
 * created by julian
 */
public interface NotificationDispatchGateway {


    OperationStatus dispatch(List<PushNotification> notifications);


}
