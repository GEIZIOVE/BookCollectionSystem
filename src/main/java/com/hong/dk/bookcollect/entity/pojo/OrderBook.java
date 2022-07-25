package com.hong.dk.bookcollect.entity.pojo;

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
@TableName("order_book")
@ApiModel(value="OrderBook对象", description="")
public class OrderBook extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "order_id", type = IdType.ASSIGN_ID)
    private String orderId;

    @ApiModelProperty(value = "书籍id")
    private Integer bookId;


}
