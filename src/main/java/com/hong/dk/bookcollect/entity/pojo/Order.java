package com.hong.dk.bookcollect.entity.pojo;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hong.dk.bookcollect.entity.pojo.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wqh
 * @since 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bcs_order")
@ApiModel(value="Order对象", description="订单表")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号")
    @TableId(value = "order_id")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "审批状态 0:待审批 1:同意 2:拒绝")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "取书时间")
    @TableField("pick_time")
    private LocalDateTime pickTime;
}
