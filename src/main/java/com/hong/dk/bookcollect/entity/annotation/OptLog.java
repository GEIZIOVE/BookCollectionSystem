package com.hong.dk.bookcollect.entity.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author wqh
 * @date 2022/7/30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * @return 操作类型
     */
    String optType() default "";

}
