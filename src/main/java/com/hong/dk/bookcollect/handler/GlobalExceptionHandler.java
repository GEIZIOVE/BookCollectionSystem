package com.hong.dk.bookcollect.handler;


import com.hong.dk.bookcollect.entity.exception.BookCollectException;

import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;


/**
 * 全局异常处理类
 * @author wqh
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理BindException异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class) // 处理参数校验异常
    public Result bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult(); // 获取参数校验结果
        return Result.build(510, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    /**
     * 处理MethodArgumentNotValidException异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // 方法参数校验异常
    public Result bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return Result.build(510, Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
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



}

