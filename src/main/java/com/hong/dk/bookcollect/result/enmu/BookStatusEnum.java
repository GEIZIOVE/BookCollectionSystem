package com.hong.dk.bookcollect.result.enmu;

import lombok.Getter;

@Getter
public enum BookStatusEnum {
    DEFAULT(-9, "ERROR"),
    WAITING_GET(0,"待取"),
    TAKEN_AWAY(1,"已取走"),
    UNDER_APPROVAL(2,"待审批");

    private final int bookStatus;
    private final String name;

    BookStatusEnum(Integer bookStatus, String name){
        this.bookStatus=bookStatus;
        this.name=name;
    }
    public static BookStatusEnum getBookStatusEnumByStatus(int bookStatus) {
        for (BookStatusEnum bookStatusEnum : BookStatusEnum.values()) {
            if (bookStatusEnum.getBookStatus() == bookStatus) {
                return bookStatusEnum;
            }
        }
        return DEFAULT;
    }

}
