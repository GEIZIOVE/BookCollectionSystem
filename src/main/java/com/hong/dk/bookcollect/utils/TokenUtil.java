package com.hong.dk.bookcollect.utils;

import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Enumeration;
import java.util.List;


public class TokenUtil {



    /**
     * 获取token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {

        String token = request.getHeader("token");

        return token;

    }

    /**
     * 获取当前登录用户id
     * @param request
     * @return
     */
    public static String getUserId(HttpServletRequest request) {
        String token = getToken(request);
        if(!StringUtils.isEmpty(token)) {
            return JwtHelper.getUserId(token);
        }
        return null;
    }


}
