package com.hong.dk.bookcollect.controller;



import com.hong.dk.bookcollect.entity.annotation.OptLog;
import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.AppealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.hong.dk.bookcollect.entity.annotation.OptTypeConst.UPLOAD;

/**
 * <p>
 * 密码申诉表(appeal) 前端控制器
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Api(tags = "密码重置")
@RestController
@RequestMapping("/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;


    @ApiOperation("登录页面重置密码")
    @OptLog(optType = UPLOAD)
    @PostMapping("/resetPassword")
    public Result resetPassword(@ApiParam("学生一卡通图片") @RequestParam("file") MultipartFile file
            , @ApiParam("学工号") @RequestParam("userid") String userId) {

        appealService.resetPassword(file, userId);
        return Result.ok("重置密码成功,请等待管理员审核");

    }

}

