package com.hong.dk.bookcollect.filter;

import com.alibaba.fastjson.JSON;

import com.hong.dk.bookcollect.entity.annotation.AccessLimit;
import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.utils.IpUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author hnz
 * @date 2022/3/23 11:21
 * @description
 */
@Log4j2
@Component
public class WebSecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 如果请求输入方法
        if (handler instanceof HandlerMethod) { // 如果是方法请求
            HandlerMethod hm = (HandlerMethod) handler;
            // 获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit != null) {
                long seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                // 本项目需求是对每个方法都加上限流功能 ip+方法名
                String key = IpUtils.getIpAddress(httpServletRequest) + hm.getMethod().getName();
                // 从redis中获取用户访问的次数
                try {
                    Long count = redisTemplate.opsForValue().increment(key, 1); // 增加1
                    if (count != null && count == 1) { // 如果是第一次访问，则设置过期时间
                        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
                    }
                    // 此操作代表获取该key对应的值自增1后的结果
//                    long q = redisService.incrExpire(key, seconds);
                    if (count > maxCount) {
                        render(httpServletResponse, Result.fail("请求过于频繁，请稍候再试"));
                        log.warn(key + "请求次数超过每" + seconds + "秒" + maxCount + "次");
                        return false;
                    }
                    return true;
                } catch (RedisConnectionFailureException e) {
                    log.warn("redis错误: " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, Result<?> result) throws Exception { // 将结果返回给前端
        response.setContentType("application/json;charset=utf-8"); // 设置返回内容类型
        OutputStream out = response.getOutputStream(); // 获取输出流
        String str = JSON.toJSONString(result); // 将对象转换为json字符串
        out.write(str.getBytes(StandardCharsets.UTF_8)); // 将字符串写入输出流
        out.flush();
        out.close();
    }

}
