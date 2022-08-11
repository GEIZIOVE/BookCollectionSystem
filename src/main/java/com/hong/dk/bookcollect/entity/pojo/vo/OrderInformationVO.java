package com.hong.dk.bookcollect.entity.pojo.vo;

import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.entity.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInformationVO {
    private String orderId;
    private String userId;
    private UserVO userVO;
    private List<Book> books;
    private Integer isAll = 0; //0为未取完,1为取完

    public OrderInformationVO(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
    }
}