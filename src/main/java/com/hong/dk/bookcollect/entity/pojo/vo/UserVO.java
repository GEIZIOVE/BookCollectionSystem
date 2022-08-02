package com.hong.dk.bookcollect.entity.pojo.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.hong.dk.bookcollect.entity.pojo.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    @ApiModelProperty(value = "学工号")
    private String userId;

    @ApiModelProperty(value = "注册人")
    private String register;

    @ApiModelProperty(value = "学院")
    private String department;


    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "头像（默认分配）")
    private String userpho;


    public UserVO(User user) {
        this.department=user.getDepartment();
        this.userId=user.getUserId();
        this.userpho=user.getUserpho();
        this.register=user.getRegister();
        this.email=user.getEmail();
        this.phone=user.getPhone();
    }
}
