package com.hong.dk.bookcollect.handler;


import com.hong.dk.bookcollect.entity.exception.BookCollectException;

import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * 全局异常处理类
 * @author wqh
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理ConstraintViolationException异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler(ConstraintViolationException e) {
//        log.debug("[constraintViolationExceptionHandler]", ex);
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(constraintViolation.getMessage());
        }
        // 包装 CommonResult 结果
        return Result.build(ResultCodeEnum.PARAM_ERROR.getCode(),
                ResultCodeEnum.PARAM_ERROR.getMessage() + ":" + detailMessage.toString());
    }
    /**
     * 处理BindException异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class) // 处理参数校验异常
    @ResponseBody
    public Result bindException(BindException e) {
//        log.info("BindException异常:{}", e.getMessage());
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ObjectError objectError : e.getAllErrors()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(objectError.getDefaultMessage());
        }
        // 包装 CommonResult 结果
        return Result.build(ResultCodeEnum.PARAM_ERROR.getCode(),
                ResultCodeEnum.PARAM_ERROR.getMessage() + ":" + detailMessage.toString());
    }

    /**
     * 处理MethodArgumentNotValidException异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // 方法参数校验异常
    @ResponseBody
    public Result bindException(MethodArgumentNotValidException e) {
//        log.info("MethodArgumentNotValidException异常:{}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult(); // 获取BindingResult
        String message = null;
        if (bindingResult.hasErrors()) { // 判断BindingResult是否有错误
            FieldError fieldError = bindingResult.getFieldError(); // 获取错误信息
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return Result.fail(message);
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
        return Result.fail(e.getMessage());
    }



}

