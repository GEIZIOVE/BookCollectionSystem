package com.hong.dk.bookcollect.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class Md5Util {
    public static String encryption(String str) {
        String md5;
        md5 = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        return md5;
    }
}