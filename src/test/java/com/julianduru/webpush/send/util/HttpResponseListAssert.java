package com.julianduru.webpush.send.util;


import org.apache.http.HttpResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian
 */
public class HttpResponseListAssert {


    public static void checkList(List<HttpResponse> responseList) {
        assertThat(responseList).isNotEmpty();

        for (HttpResponse response : responseList) {
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
        }
    }


}
