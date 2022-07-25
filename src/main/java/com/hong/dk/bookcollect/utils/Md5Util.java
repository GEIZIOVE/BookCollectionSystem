package com.hong.dk.bookcollect.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class Md5Util {
    public static String encryption(String str) {
        String md5 = "";
        try {
            md5 = DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5;
    }
}