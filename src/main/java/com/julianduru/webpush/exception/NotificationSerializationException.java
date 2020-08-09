package com.julianduru.webpush.exception;


/**
 * created by julian
 */
public class NotificationSerializationException extends RuntimeException {


    public NotificationSerializationException() {
    }

    public NotificationSerializationException(String message) {
        super(message);
    }

    public NotificationSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationSerializationException(Throwable cause) {
        super(cause);
    }

}
