package com.hong.dk.bookcollect.entity.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.entity.pojo.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO implements Serializable {

    @ApiModelProperty(value = "订单编号")
    private String orderId;

    @ApiModelProperty(value = "审批状态 0:待审批 1:同意 2:拒绝")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "订单提交时间")
    private LocalDateTime pickTime;

    @ApiModelProperty(value = "书籍列表")
    private List<Book> bookList;

    public OrderVO(Order order) {
        this.orderId = order.getOrderId();
        this.status = order.getStatus();
        this.userId = order.getUserId();
        this.pickTime = order.getPickTime();
    }
}
