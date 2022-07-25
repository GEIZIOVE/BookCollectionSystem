package com.hong.dk.bookcollect.controller;



import com.hong.dk.bookcollect.entity.pojo.User;
import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
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
    public Result login(@ApiParam("用户学工号") @RequestParam("userid") String userId,@ApiParam("密码") @RequestParam("password") String password) {
        Map<String, Object> map = userService.login(userId, password);
        return Result.ok(map);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
         userService.logout(request);
         return Result.ok();


    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@Validated @RequestBody User user, BindingResult result ) {
        //判断是否有error
        if (result.hasErrors()) {
            //获取全局与属性的错误
            //result.getAllErrors();
            //只获取属性校验的错误
            Map<String, Object> map = new HashMap<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                map.put("msg", fieldError.getDefaultMessage());
            }
            return Result.fail(map);
        }
        Boolean flag = userService.register(user);
        if (flag) {
            return Result.ok("注册成功");
        } else {
            return Result.fail("注册失败");
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePassword/{oldPassword}/{newPawssword}")
    public Result<String> updatePassword (@ApiParam("原密码") @PathVariable String oldPassword,
                                           @ApiParam("新密码")  @PathVariable String newPawssword,
                                             HttpServletRequest request) {

        Boolean flag = userService.updatePassword(oldPassword, newPawssword,request);
        if (flag) {
            return Result.ok("修改成功");
        } else {
            return Result.fail("修改失败");
        }

    }

    @ApiOperation("用户上传头像")
    @PostMapping("/uploadAvatar")
public Result uploadAvatar(@ApiParam("头像") @RequestParam("file") MultipartFile file,
                               HttpServletRequest request) {
        Boolean flag = userService.uploadAvatar(file, request);
        if (flag) {
            return Result.ok("上传成功");
        } else {
            return Result.fail("上传失败");
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUser")
    public Result<User> getUser(HttpServletRequest request) {
        User user = userService.getUser(request);
        return Result.ok(user);
    }





}

