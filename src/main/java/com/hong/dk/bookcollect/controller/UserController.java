package com.hong.dk.bookcollect.controller;



import com.hong.dk.bookcollect.entity.annotation.TokenToUser;
import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.entity.pojo.UserToken;
import com.hong.dk.bookcollect.entity.pojo.param.UpdateUserPasswordParam;
import com.hong.dk.bookcollect.entity.pojo.param.UserLoginParam;
import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.Map;

/**
 * <p>
 * 用户表(user) 前端控制器
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Api(value = "用户表(user)接口", tags = "用户表(user)接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody @Valid UserLoginParam userLoginParam, BindingResult result) {
        Map<String, Object> map = userService.login(userLoginParam.getUserId(), userLoginParam.getPassword());
        return Result.ok(map);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result logout(@TokenToUser UserToken user) {
         userService.logout(user.getUserId());
         return Result.ok();


    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register( @RequestBody User user) {

        Boolean flag = userService.register(user);
        if (flag) {
            return Result.ok("注册成功");
        } else {
            return Result.fail("注册失败");
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePassword/{oldPassword}/{newPawssword}")
    public Result<String> updatePassword (@RequestBody @Valid UpdateUserPasswordParam userPasswordParam,@TokenToUser UserToken user) {
        Boolean flag = userService.updatePassword(userPasswordParam.getOriginalPassword(),userPasswordParam.getNewPassword(), user.getUserId());
        if (flag) {
            return Result.ok("修改成功");
        } else {
            return Result.fail("修改失败");
        }

    }

    @ApiOperation("用户上传头像")
    @PostMapping("/uploadAvatar")
public Result uploadAvatar(@ApiParam("头像") @RequestParam("file") MultipartFile file,
                           @TokenToUser UserToken user) {
        Boolean flag = userService.uploadAvatar(file, user.getUserId());
        if (flag) {
            return Result.ok("上传成功");
        } else {
            return Result.fail("上传失败");
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUser")
    public Result<User> getUser(@TokenToUser UserToken user) {

        return Result.ok( userService.getUser(user.getUserId()));

    }





}

