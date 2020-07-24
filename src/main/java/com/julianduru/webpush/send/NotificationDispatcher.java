package com.julianduru.webpush.send;


import com.julianduru.webpush.entity.Notification;
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


    Future<List<HttpResponse>> sendNotification(Notification notification) ;



    static HttpResponse defaultSuccessNotifResponse(String message) {
        return new BasicHttpResponse(new BasicStatusLine(new HttpVersion(1, 1), 201, message));
    }


    static HttpResponse defaultFailedNotifResponse(String message) {
        return new BasicHttpResponse(new BasicStatusLine(new HttpVersion(1, 1), 405, message));
    }


}
