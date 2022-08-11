package com.hong.dk.bookcollect.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.config.MinioConfig;
import com.hong.dk.bookcollect.constant.MailInfoConst;
import com.hong.dk.bookcollect.constant.MinIOConst;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.entity.pojo.dto.EmailDTO;
import com.hong.dk.bookcollect.entity.pojo.param.UserRegisterParam;
import com.hong.dk.bookcollect.entity.pojo.vo.UserVO;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.UserMapper;
import com.hong.dk.bookcollect.result.Consts;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import com.hong.dk.bookcollect.service.UserService;
import com.hong.dk.bookcollect.utils.*;
import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.hong.dk.bookcollect.constant.MQPrefixConst.EMAIL_EXCHANGE;
import static com.hong.dk.bookcollect.utils.CommonUtils.checkEmail;
import static com.hong.dk.bookcollect.utils.CommonUtils.getRandomCode;

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
    @Autowired
    private RabbitTemplate rabbitTemplate;




    Map<String,Object> map = new HashMap<>();

    @Override
    public Map<String, Object> login(String userId, String password) {
//         逻辑1：如果用户已经登录就不能再次登录
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
//        //逻辑2：如果用户没有登录，可以下线另一个
        //根据userID查询用户信息
        User user =  baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));

        if (user == null) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        } else if (!user.getPassword().equals(Md5Util.encryption(password))) {
            Asserts.fail(ResultCodeEnum.DATA_ERROR);
        } else {
            String token = JwtHelper.createToken(user.getUserId(), user.getRegister()); //生成token
            int expire = 2;  //设置存入redis的token有效期2小时
            redisTemplate.opsForValue().set(userId+":"+userId,token, expire, TimeUnit.HOURS); //参数依次为 key,value,过期时间,时间单位
            map.put("msg", "登录成功");
            map.put("token",token);
        }
        return map;
    }

    @Override
    public void logout(String userId) {
        if (Boolean.FALSE.equals(redisTemplate.delete(userId + ":" + userId))){
            Asserts.fail(ResultCodeEnum.FAIL);
        }
    }
    @Override
    public UserVO getUser(String userId) {

        //先查询redis中是否存在该用户信息
        UserVO userVO = JSON.parseObject(redisTemplate.opsForValue().get(userId+":"+"userInfo"), UserVO.class);
        if( null != userVO ) {
            return userVO;
        }
        //根据userid查询用户信息
        User user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));

        UserVO userVO_new = Optional.ofNullable(user).map(UserVO::new).orElse(null);

        if (userVO_new == null) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }

        //将用户信息缓存到redis中,命名空间为user,key为userid,value为user对象
        redisTemplate.opsForValue().set(userId+":"+"userInfo", JSON.toJSONString(userVO_new),30,TimeUnit.SECONDS);
        return userVO_new;
    }

    @Override
    public Boolean uploadAvatar(MultipartFile file, String userId) {
        //根据userid查询用户信息
        User user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
        if (null == user) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }

        String fileName = minioUtil.upload(file, MinIOConst.MINIO_BUCKET_NAME_AVATAR,userId);
        if (null != fileName) {
            user.setUserpho(prop.getEndpoint() + "/" + MinIOConst.MINIO_BUCKET_NAME_AVATAR + "/" + fileName);
            baseMapper.updateById(user);
            return true;
        }
        return false;
    }

    @Override
    public void sendCode(String email) {
        //生成验证码
        // 校验账号是否合法
        if (!checkEmail(email)) {
            Asserts.fail("请输入正确的邮箱",202);
        }
        // 生成六位随机验证码发送
        String code = getRandomCode();
        // 发送验证码
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject(MailInfoConst.MAIL_SUBJECT_REGISTER)
                .content(code)
                .build();
        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // 将验证码存入redis，设置过期时间为10分钟
        redisTemplate.opsForValue().set("code:"+email, code, 10, TimeUnit.MINUTES);
    }


    @Override
    public void register(UserRegisterParam userRegisterParam) {
        // 校验账号是否合法
        if (!userRegisterParam.getCode().equals(redisTemplate.opsForValue().get("code:"+userRegisterParam.getEmail()))) {
            Asserts.fail("验证码错误！",201);
        }
        // 校验邮箱是否已存在
        User userQuery_email = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getEmail, userRegisterParam.getEmail()));
        if (null != userQuery_email) {
            Asserts.fail("邮箱已被绑定！",201);
        }
        //根据userid查询用户信息
        User userQuery_userId = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userRegisterParam.getUserId()));
        if (null != userQuery_userId) {
            Asserts.fail(ResultCodeEnum.USER_EXIST);
        } else {
            User user = new User();
            BeanUtil.copyProperties(userRegisterParam, user);
            user.setUserpho(Consts.USER_DEFAULT_IMAGE); //默认头像
            user.setPassword(Md5Util.encryption(userRegisterParam.getPassword())); //密码加密
            if(baseMapper.insert(user)<0) {
                Asserts.fail("未知错误",201);
            }
        }
    }

    @Override
    public Boolean updatePassword(String oldPassword, String newPawssword, String userId) {
            //根据userid查询用户信息
        User user = baseMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
            if (null == user) {
                Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
            } else if (!user.getPassword().equals(Md5Util.encryption(oldPassword))) {
                Asserts.fail(ResultCodeEnum.PASSWORD_ERROR);
            } else {
                user.setPassword(Md5Util.encryption(newPawssword));
                int update = baseMapper.updateById(user);
                //更新redis的缓存
                redisTemplate.opsForValue().set(userId+":"+"userInfo", JSON.toJSONString(user),30,TimeUnit.SECONDS);
                return update > 0;
            }
        return false;
    }


}
