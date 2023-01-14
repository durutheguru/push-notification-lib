package com.julianduru.webpush.send.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian on 12/01/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {


    public enum Type {

        BYTES, STRING,

    }


    private Type messageType;


    private T data;


}

