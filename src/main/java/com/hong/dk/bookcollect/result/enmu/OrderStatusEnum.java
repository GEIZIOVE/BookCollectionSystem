package com.hong.dk.bookcollect.result.enmu;

import lombok.Getter;

@Getter
public enum OrderStatusEnum { //审批状态 0:待审批 1:同意 2:拒绝
    DEFAULT(-9, "ERROR"),
    WAITING_GET(0,"待审批"),
    TAKEN_AWAY(1,"同意"),
    UNDER_APPROVAL(2,"拒绝");

    private int orderStatus;
    private String name;

    OrderStatusEnum(Integer bookStatus, String name){
        this.orderStatus=bookStatus;
        this.name=name;
    }
    public static OrderStatusEnum getOrderStatusEnumByStatus(int orderStatus) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getOrderStatus() == orderStatus) {
                return orderStatusEnum;
            }
        }
        return DEFAULT;
    }

}
