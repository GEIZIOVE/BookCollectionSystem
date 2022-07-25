package com.hong.dk.bookcollect.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hong.dk.bookcollect.entity.pojo.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 密码申诉表(appeal)
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("appeal")
@ApiModel(value="Appeal对象", description="密码申诉表(appeal)")
public class Appeal extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "学工号")
    private String userId;

    @ApiModelProperty(value = "申诉时间")
    private LocalDateTime appealTime;

    @ApiModelProperty(value = "凭证照片")
    private String photo;

    @ApiModelProperty(value = "0表示未审核，1表示审核成功，2表示拒绝，默认为0")
    private Integer auditStatus;




}
