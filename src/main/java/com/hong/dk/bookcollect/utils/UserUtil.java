package com.hong.dk.bookcollect.utils;

import com.hong.dk.bookcollect.entity.pojo.param.UserThreadParam;

import java.util.Objects;

/**
 *
 *
 * @author wqh
 * @date 2022/08/1
 **/
public class UserUtil {

    private static final ThreadLocal<UserThreadParam> USER_HOLDER = new ThreadLocal<>(); //ThreadLocal<UserThreadParam>表示定义一个线程局部变量，该变量只存在于当前线程中，在线程结束后，该变量被销毁。

    public static void setUserThreadParam(UserThreadParam userThreadParam) {
        USER_HOLDER.set(userThreadParam); //设置线程局部变量
    }

    public static UserThreadParam getUserThreadParam() {
        UserThreadParam userThreadParam = USER_HOLDER.get(); //获取线程局部变量
        if (Objects.isNull(userThreadParam)) {
            setUserThreadParam(new UserThreadParam());
        }
        return USER_HOLDER.get();
    }

    public static String getUserId() {
        return getUserThreadParam().getUserId();
    }

    public static String getRegister() {
        return getUserThreadParam().getRegister();
    }


    public static void remove() {
        USER_HOLDER.remove(); //移除线程局部变量
    }
}
