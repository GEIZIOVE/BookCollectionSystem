package com.hong.dk.bookcollect.service;

import com.hong.dk.bookcollect.entity.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * <p>
 * 用户表(user) 服务类
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userId
     * @param password
     * @return
     */
    Map<String,Object> login(String userId, String password);

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    Boolean register(User user);

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPawssword
     * @return
     */
    Boolean updatePassword(String oldPassword, String newPawssword, String userId);


    void logout(String userId);


    User getUser(String userId);

    Boolean uploadAvatar(MultipartFile file, String userId);
}
