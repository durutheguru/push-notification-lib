package com.julianduru.webpush.exception;


/**
 * created by julian
 */
public class ServerSentEventException extends Exception {


    public ServerSentEventException() {
    }

    public ServerSentEventException(String message) {
        super(message);
    }

    public ServerSentEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerSentEventException(Throwable cause) {
        super(cause);
    }

    public ServerSentEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
