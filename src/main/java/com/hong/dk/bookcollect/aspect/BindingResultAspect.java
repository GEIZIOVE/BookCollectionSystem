//package com.hong.dk.bookcollect.aspect;
//
//import com.hong.dk.bookcollect.result.Result;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//@Order(2)
//public class BindingResultAspect {
//    @Pointcut("execution(public * com.hong.dk.bookcollect.controller.*.*(..))") // 定义一个切入点，表示要切入的方法
//    public void BindingResult() { //方法体为空，只是为了让切入点生效
//    }
//    @Around("BindingResult()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object[] args = joinPoint.getArgs(); // 获取方法参数
//        for (Object arg : args) {
//            if (arg instanceof BindingResult) { // 判断参数是否是BindingResult类型
//                BindingResult result = (BindingResult) arg;
//                if (result.hasErrors()) { // 判断BindingResult是否有错误
//                    Map<String, String> map = new HashMap<>();
//                    for (FieldError fieldError : result.getFieldErrors()) {
//                        map.put(fieldError.getField(), fieldError.getDefaultMessage());
//                    }
//                    return Result.fail(map);
//                }
//            }
//        }
//        return joinPoint.proceed(); // proceed执行方法并且返回结果
//    }
//}