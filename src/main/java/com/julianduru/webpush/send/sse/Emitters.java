package com.julianduru.webpush.send.sse;


import com.julianduru.webpush.exception.ServerSentEventException;
import com.julianduru.webpush.send.api.OperationStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * created by julian
 */
public interface Emitters {


    Map<String, UserIDEmittersContainer> getEmitterMap();


    Flux<Object> add(String userId, String token) throws IllegalStateException;


    List<OperationStatus<String>> send(String authUserId, Object obj);


    List<OperationStatus<String>> send(Object obj) throws ServerSentEventException;


}
