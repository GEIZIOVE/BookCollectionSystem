package com.hong.dk.bookcollect.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.config.MinioConfig;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.UserMapper;
import com.hong.dk.bookcollect.result.Consts;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import com.hong.dk.bookcollect.service.UserService;
import com.hong.dk.bookcollect.utils.*;
import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表(user) 服务实现类
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig prop;



    Map<String,Object> map = new HashMap<>();

    @Override
    public Map<String, Object> login(String userId, String password) {

//        String dubToken = redisTemplate.opsForValue().get(userId + ":" + userId);
//        if (dubToken != null) {
//            //如果dubToken不为空，判断token是否过期
//            if (JwtHelper.isTokenExpired(dubToken)) {
//                //如果过期，删除redis中的token
//                redisTemplate.delete(userId + ":" + userId);
//            } else {
//                Asserts.fail("请勿重复登录",201);
//            }
//        }
        //根据userID查询用户信息
        User user =  baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));

        if (user == null) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        } else if (!user.getPassword().equals(password)) {
            Asserts.fail(ResultCodeEnum.DATA_ERROR);
        } else {
            String token = JwtHelper.createToken(user.getUserId(), user.getRegister()); //生成token
            Integer expire = 2;  //设置存入redis的token有效期2小时
            redisTemplate.opsForValue().set(userId+":"+userId,token, expire, TimeUnit.HOURS); //参数依次为 key,value,过期时间,时间单位
            map.put("msg", "登录成功");
            map.put("token",token);
        }
        return map;
    }

    @Override
    public void logout(String userId) {
        if (!redisTemplate.delete(userId+":"+userId)){
            Asserts.fail(ResultCodeEnum.FAIL);
        }
    }
    @Override
    public User getUser(String userId) {

        //先查询redis中是否存在该用户信息
        User user = JSON.parseObject((String)redisTemplate.opsForValue().get(userId+":"+"userInfo"), User.class);
        if( null != user ) {
            return user;
        }
        //根据userid查询用户信息
         user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
        if (user == null) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }
        //将用户信息缓存到redis中,命名空间为user,key为userid,value为user对象
        redisTemplate.opsForValue().set(userId+":"+"userInfo", JSON.toJSONString(user),30,TimeUnit.SECONDS);
        return user;
    }

    @Override
    public Boolean uploadAvatar(MultipartFile file, String userId) {
        //根据userid查询用户信息
        User user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
        if (null == user) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }

        String fileName = minioUtil.upload(file);
        if (null != fileName) {
            user.setUserpho(prop.getEndpoint() + "/" + prop.getBucketName() + "/" + fileName);
            baseMapper.updateById(user);
            return true;
        }
        return false;
    }


    @Override
    public Boolean register(User user) {
        //根据userid查询用户信息
        User userQuery = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, user.getUserId()));
        if (null != userQuery) {
            Asserts.fail(ResultCodeEnum.USER_EXIST);
        } else {
            user.setUserpho(Consts.USER_DEFAULT_IMAGE); //默认头像
            return baseMapper.insert(user) > 0;
        }

        return false;
    }

    @Override
    public Boolean updatePassword(String oldPassword, String newPawssword, String userId) {
            //根据userid查询用户信息
        User user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
            if (null == user) {
                Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
            } else if (!user.getPassword().equals(oldPassword)) {
                Asserts.fail(ResultCodeEnum.PASSWORD_ERROR);
            } else {
                user.setPassword(newPawssword);
                int update = baseMapper.updateById(user);
                //更新redis的缓存
                redisTemplate.opsForValue().set(userId+":"+"userInfo", JSON.toJSONString(user),30,TimeUnit.SECONDS);
                return update > 0;
            }
        return false;
    }



}
