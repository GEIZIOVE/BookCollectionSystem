package com.hong.dk.bookcollect.filter;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hong.dk.bookcollect.entity.annotation.TokenToUser;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.entity.pojo.UserToken;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.UserMapper;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserMapper userMapper;


    public TokenToUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToUser.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
        if (null != user){
            UserToken userToken = new UserToken();
            userToken.setToken(token);
            userToken.setUserId(user.getUserId());
            return userToken;
        }else {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }
        return null;

    }

}
