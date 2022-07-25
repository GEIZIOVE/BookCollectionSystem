package com.hong.dk.bookcollect.service;

import com.hong.dk.bookcollect.entity.pojo.Appeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 密码申诉表(appeal) 服务类
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
public interface AppealService extends IService<Appeal> {
    /**
     * 登录页面重置密码
     *
     * @param file
     * @param userId
     * @return
     */
    void resetPassword(MultipartFile file, String userId);

}
