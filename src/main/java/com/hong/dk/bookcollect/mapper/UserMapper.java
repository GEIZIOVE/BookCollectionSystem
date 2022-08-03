package com.hong.dk.bookcollect.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hong.dk.bookcollect.entity.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表(user) Mapper 接口
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
