package com.hong.dk.bookcollect.handler;


import com.hong.dk.bookcollect.entity.exception.BookCollectException;

import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.result.ResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理类
 * @author wqh
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理Exception异常,这里全局返回Result对象
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 处理FyException异常,这里全局返回Result对象
     * @param e
     * @return
     */
    @ExceptionHandler(BookCollectException.class)
    @ResponseBody
    public Result error(BookCollectException e) {
        if (e.getResultCodeEnum() != null) {
            return Result.build(e.getResultCodeEnum().getCode(),
                    e.getResultCodeEnum().getMessage());
        }
        return Result.build(e.getCode(),e.getMessage());
    }

    /**
     * 处理ExpiredJwtException异常,全局返回Result对象
     * @param e
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public Result error(ExpiredJwtException e) {
        return Result.build(ResultCodeEnum.SIGN_EXPIRED.getCode(),
                ResultCodeEnum.SIGN_EXPIRED.getMessage());
    }

}

