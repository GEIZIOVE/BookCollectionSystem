package com.hong.dk.bookcollect.filter;

import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.result.ResultCodeEnum;

import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class AuthHandlerInterceptor  extends HandlerInterceptorAdapter {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    
    /**
     * 权限认证的拦截操作.
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             @Qualifier("jacksonObjectMapper") Object object) throws Exception {
//        log.info("=======进入拦截器========");
        // 如果不是映射到方法直接通过,可以访问资源.
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //为空就返回错误
        String token = httpServletRequest.getHeader("token");
        if (null == token || "".equals(token.trim())) {
            Asserts.fail(ResultCodeEnum.LOGIN_AUTH);
        }
        //判断token是否过期
        if (JwtHelper.isTokenExpired(token)) {
            Asserts.fail(ResultCodeEnum.SIGN_EXPIRED);
        }
        //获取token中的用户id
        String userId = JwtHelper.getUserId(token);
        //通过userId查询redis中的token2
        String token2 = (String) redisTemplate.opsForValue().get(userId+":"+userId);
        //验证token2是否为空
        if ( null == token2 || "".equals(token2.trim()) ) {
            redisTemplate.opsForValue().set(userId+":"+userId,token,3600 * 3,TimeUnit.SECONDS);
        }
        //token2的过期时间，如果小于1小时，则重新设置过期时间为3小时
        if ( redisTemplate.getExpire(userId+":"+userId, TimeUnit.SECONDS ) < 3600) {
             redisTemplate.expire(userId+":"+userId, 3600 * 3, TimeUnit.SECONDS);
        }
//        log.info("===========放行=========");
        return true;
    }

}