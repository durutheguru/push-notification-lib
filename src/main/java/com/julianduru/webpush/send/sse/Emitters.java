package com.julianduru.webpush.send.sse;


import com.julianduru.webpush.exception.ServerSentEventException;
import org.apache.http.HttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * created by julian
 */
public interface Emitters {


    Map<String, List<SseEmitter>> getEmitterMap();


    SseEmitter add(SseEmitter emitter) throws IllegalStateException;


    List<HttpResponse> send(String authUserId, Object obj);


    List<HttpResponse> send(Object obj) throws ServerSentEventException;


}
