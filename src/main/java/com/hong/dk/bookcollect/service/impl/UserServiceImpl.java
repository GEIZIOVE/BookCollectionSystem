package com.hong.dk.bookcollect.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.config.MinioConfig;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.UserMapper;
import com.hong.dk.bookcollect.result.ResultCodeEnum;
import com.hong.dk.bookcollect.service.UserService;
import com.hong.dk.bookcollect.utils.*;
import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
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

        String dubToken = redisTemplate.opsForValue().get(userId + ":" + userId);
        if (dubToken != null) {
            //如果dubToken不为空，判断token是否过期
            if (JwtHelper.isTokenExpired(dubToken)) {
                //如果过期，删除redis中的token
                redisTemplate.delete(userId + ":" + userId);
            } else {
                Asserts.fail("请勿重复登录",201);
            }
        }

        //根据userID查询用户信息
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));

        if (user == null) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        } else if (!user.getPassword().equals(password)) {
            Asserts.fail(ResultCodeEnum.DATA_ERROR);
        } else {
            String token = JwtHelper.createToken(user.getUserId(), user.getRegister()); //生成token
            map.put("msg", "登录成功");
            map.put("token",token);
            Integer expire = 2;  //设置token有效期2小时
            //设置 token 至 redis
            redisTemplate.opsForValue().set(userId+":"+userId,token, expire, TimeUnit.HOURS); //参数依次为 key,value,过期时间,时间单位
        }
        return map;
    }

    @Override
    public void logout(HttpServletRequest request) {
        //清除redis中的token
        String token = TokenUtil.getToken(request);
        String userId = JwtHelper.getUserId(token);

        if (!redisTemplate.delete(userId+":"+userId)){
            Asserts.fail("退出登录失败",201);
        }


    }


    @Override
    public User getUser(HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);
        //先查询redis中是否存在该用户信息
        User user = JSON.parseObject((String)redisTemplate.opsForValue().get(userId+":"+"userInfo"), User.class);
        if( user != null ) {
            return user;
        }
        //根据userid查询用户信息
         user = baseMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));
        if (user == null) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }
        //将用户信息缓存到redis中,命名空间为user,key为userid,value为user对象
        redisTemplate.opsForValue().set(userId+":"+"userInfo", JSON.toJSONString(user),30,TimeUnit.SECONDS);
        return user;
    }

    @Override
    public Boolean uploadAvatar(MultipartFile file, HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);
        //根据userid查询用户信息
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));
        if (user == null) {
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
        User user1 = baseMapper.selectOne(new QueryWrapper<User>().eq("user_id", user.getUserId()));
        if (user1 != null) {
            Asserts.fail(ResultCodeEnum.USER_EXIST);
        } else {
            user.setUserpho("http://106.53.124.182:9000/public/2022/07/24/969e3166-094c-445c-afba-c6c2cfa4ae23.jfif"); //默认头像
            int insert = baseMapper.insert(user);
            return insert > 0;
        }

        return false;
    }

    @Override
    public Boolean updatePassword(String oldPassword, String newPawssword, HttpServletRequest request) {
        //查询redis中的token获取userId
        String token = TokenUtil.getToken(request);
        String userId = JwtHelper.getUserId(token);
//        System.out.println("userId"+userId);

            //根据userid查询用户信息
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("user_id", userId));
            if (user == null) {
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
