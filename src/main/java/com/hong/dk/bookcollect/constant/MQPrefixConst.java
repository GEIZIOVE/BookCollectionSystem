package com.hong.dk.bookcollect.constant;

/**
 * mqprefix常量
 * mq常量
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
public class MQPrefixConst {

    /**
     * canal交换机
     */
    public static final String CANAL_EXCHANGE = "canal.exchange";

    /**
     * canal队列 监听book表
     */
    public static final String CANAL_BOOK_QUEUE = "canal.book.queue";

    /**
     * canal队列 监听appeal表
     */
    public static final String CANAL_APPEAL_QUEUE = "canal.appeal.queue";

    /**
     * canal队列 监听bcs_order表
     */
    public static final String CANAL_ORDER_QUEUE = "canal.order.queue";

    /**
     * Key
     */
    public static final String CANAL_BOOK_KEY = "bcs_book";

    public static final String CANAL_APPEAL_KEY = "bcs_appeal";

    public static final String CANAL_ORDER_KEY = "bcs_bcs_order";




    /**
     * email交换机
     */
    public static final String EMAIL_EXCHANGE = "email.exchange";

    /**
     * 邮件队列
     */
    public static final String EMAIL_QUEUE = "email.queue";

}
