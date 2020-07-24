package com.julianduru.webpush.send.util;


import org.apache.http.HttpResponse;
import org.junit.Assert;

import java.util.List;

/**
 * created by julian
 */
public class HttpResponseListAssert {


    public static void checkList(List<HttpResponse> responseList) {
        Assert.assertTrue(!responseList.isEmpty());

        for (HttpResponse response : responseList) {
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());
        }
    }


}
