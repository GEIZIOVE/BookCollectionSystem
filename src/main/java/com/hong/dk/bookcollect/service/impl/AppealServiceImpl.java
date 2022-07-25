package com.hong.dk.bookcollect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hong.dk.bookcollect.config.MinioConfig;
import com.hong.dk.bookcollect.entity.pojo.Appeal;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.AppealMapper;
import com.hong.dk.bookcollect.mapper.UserMapper;
import com.hong.dk.bookcollect.result.ResultCodeEnum;
import com.hong.dk.bookcollect.service.AppealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * <p>
 * 密码申诉表(appeal) 服务实现类
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Service
public class AppealServiceImpl extends ServiceImpl<AppealMapper, Appeal> implements AppealService {

    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfig prop;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void resetPassword(MultipartFile file, String userId) {
        //根据userid查询用户信息
        User user =  userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUserId, userId));
        if (null == user) {
            Asserts.fail(ResultCodeEnum.USER_NOT_EXIST);
        }

        //查询appeal表中是否有该用户的申诉记录
        Appeal appeal =  baseMapper.selectOne(Wrappers.lambdaQuery(Appeal.class).eq(Appeal::getUserId, userId));
        if(null != appeal && appeal.getAuditStatus() == 0){
            Asserts.fail(ResultCodeEnum.APPEAL_ING);
        }
        Appeal appeal1 = new Appeal();//创建一个申诉对象
        appeal1.setUserId(userId);//设置用户id
        appeal1.setAppealTime(LocalDateTime.now());//设置申诉时间
        //将图片上传到minio服务器
        String objectName = minioUtil.upload(file);
        if (null != objectName) {
            appeal1.setPhoto( prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName ); ;
        }
        // 将申诉信息插入数据库
        if (!(this.baseMapper.insert(appeal1) > 0)){
            Asserts.fail(ResultCodeEnum.FAIL);
        }
    }
}
