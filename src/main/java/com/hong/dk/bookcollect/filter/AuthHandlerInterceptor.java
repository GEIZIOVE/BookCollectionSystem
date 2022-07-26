package com.hong.dk.bookcollect.filter;

import com.hong.dk.bookcollect.entity.pojo.param.UserThreadParam;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import com.hong.dk.bookcollect.utils.UserUtil;
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

import java.util.Optional;
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
                             @Qualifier("jacksonObjectMapper") Object object) {

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
        //获取token中的用户名
        String userName = JwtHelper.getUserName(token);
        //通过userId查询redis中的token2
        String token_redis = redisTemplate.opsForValue().get(userId+":"+userId);
        //验证token_redis是否为空，如果为空则将token2存入redis中
        if ( null == token_redis || "".equals(token_redis.trim()) ) {
            redisTemplate.opsForValue().set(userId+":"+userId,token,3600 * 3,TimeUnit.SECONDS);
        }
        if(!token.equals(token_redis)){
            Asserts.fail(ResultCodeEnum.REMOTE_LOGIN);
        }
        //token2的过期时间，如果小于1小时，则重新设置过期时间为3小时
        Optional.ofNullable(redisTemplate.getExpire(userId+":"+userId,TimeUnit.SECONDS)).ifPresent(expire -> {
            if(expire < 3600){
                redisTemplate.expire(userId+":"+userId,3600 * 3,TimeUnit.SECONDS);
            }
        });
        //将用户id和用户名存入线程变量
        UserThreadParam userThreadParam = new UserThreadParam();
        userThreadParam.setUserId(userId);
        userThreadParam.setRegister(userName);
        UserUtil.setUserThreadParam(userThreadParam);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserUtil.remove();
    }

}