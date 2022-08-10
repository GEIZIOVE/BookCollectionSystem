package com.hong.dk.bookcollect.constant;

/**
 * redis常量
 *
 * @author wqh
 * @date 2022/08/01
 */
public class RedisPrefixConst {

    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60;

    /**
     * 验证码
     */
    public static final String USER_CODE_KEY = "code:";


}
