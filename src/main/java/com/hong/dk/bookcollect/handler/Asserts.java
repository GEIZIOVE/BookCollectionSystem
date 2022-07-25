package com.hong.dk.bookcollect.handler;

import com.hong.dk.bookcollect.entity.exception.BookCollectException;
import com.hong.dk.bookcollect.result.ResultCodeEnum;

public class Asserts {
    public static void fail(String message,Integer code) {
        throw new BookCollectException(message,code);
    }

    public static void fail(ResultCodeEnum resultCodeEnum) {
        throw new BookCollectException(resultCodeEnum);
    }
}