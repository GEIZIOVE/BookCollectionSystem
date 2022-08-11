package com.hong.dk.bookcollect.result.enmu;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    PARAM_ERROR( 202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),

    DATA_UPDATE_ERROR(205, "数据版本异常"),

    LOGIN_AUTH(208, "请先登录"),
    PERMISSION(209, "没有权限"),

    USER_NOT_EXIST(221, "用户不存在"),
    DATA_ERROR(222, "账号或密码错误"),
    USER_EXIST(223, "用户已存在"),
    PASSWORD_ERROR(224, "原密码错误"),
//    "申诉正在审核中，请耐心等待",201
    APPEAL_ING(225, "申诉正在审核中，请耐心等待哦"),
    REMOTE_LOGIN(226,"账户已在别处登录,请重新登陆" ),
    SIGN_EXPIRED(300, "签名过期");




    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
