package com.julianduru.webpush.controller.sse;


import com.julianduru.webpush.NotificationConstant;
import com.julianduru.webpush.send.sse.Emitters;
import com.julianduru.webpush.send.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

/**
 * created by julian
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = NotificationConstant.SSE_API_PREFIX)
public class NotificationEventController {


    @Value("${spring.mvc.async.request-timeout}")
    private Long sseTimeout;


    private final Emitters sseEmitters;


    @GetMapping(value = "/notification", produces = { MediaType.TEXT_EVENT_STREAM_VALUE })
    public SseEmitter handleNotificationsRequest(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");

        return sseEmitters.add(new SseEmitter(sseTimeout));
    }


}
