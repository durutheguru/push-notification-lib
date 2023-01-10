package com.julianduru.webpush.send.util;


import com.julianduru.webpush.send.api.OperationStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * created by julian
 */
public class HttpResponseListAssert {


    public static void checkList(List<OperationStatus<String>> responseList) {
        assertThat(responseList).isNotEmpty();

        for (var response : responseList) {
            assertTrue(response.is(OperationStatus.Value.SUCCESS));
        }
    }


}
