package com.julianduru.webpush.send;


import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.util.*;
import java.util.concurrent.Future;

/**
 * created by julian
 */
public interface NotificationDispatcher {


    List<OperationStatus<String>> sendNotification(PushNotification notification) ;



}
