package com.hong.dk.bookcollect.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.hong.dk.bookcollect.entity.pojo.base.BaseEntity;
import com.hong.dk.bookcollect.entity.pojo.param.UserRegisterParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 用户表(user)
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@ApiModel(value="User对象", description="用户表(user)")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "学工号")
    @TableField("user_id")
    private String userId;


    @ApiModelProperty(value = "注册人")
    @TableField("register")
    private String register;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "联系方式")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "0为个人账号,1为学院账号")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "学院")
    @TableField("department")
    private String department;


    @ApiModelProperty(value = "头像（默认分配）")
    @TableField("userpho")
    private String userpho;


    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;


}
