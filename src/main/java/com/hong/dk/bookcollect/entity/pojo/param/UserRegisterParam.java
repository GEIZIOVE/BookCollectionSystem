package com.hong.dk.bookcollect.entity.pojo.param;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserRegisterParam implements Serializable {

    @ApiModelProperty(value = "学工号")
    @Size(min = 10 ,max = 12)
    private String userId;

    @ApiModelProperty(value = "注册人")
    @NotEmpty(message = "注册人不能为空")
    @Size(min = 1,max = 10)
    private String register;

    @ApiModelProperty(value = "学院")
    @NotEmpty(message = "学院不能为空")
    private String department;


    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "联系方式")
    @NotEmpty(message = "联系方式不能为空")
    private String phone;


    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    @Size(min = 4 ,max = 16)
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotEmpty(message = "验证码不能为空")
    @Size(min = 6 ,max = 6 )
    private String code;

}
