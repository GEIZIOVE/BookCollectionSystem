package com.hong.dk.bookcollect.utils;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.Random;

public class OrderUtil {


    /**
     * 测试
     */
    public static void main(String[] args) {
//        long startTime = System.currentTimeMillis();
//        SnowFlakeUtil idWorker = new SnowFlakeUtil(0, 0);
//        Set set = new HashSet();
//        for (int i = 0; i < 10000000; i++) {
//            long id = idWorker.nextId();
//            set.add(id);
//            System.out.println("id----"+i+":"+id);
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("set.size():" + set.size());
//        System.out.println("endTime-startTime:" + (endTime - startTime));

//        String s = timestampConversionDate(1530607760000L);

        String orderNo = getOrderNo();
        System.out.println(orderNo);
    }

    /**
     * 获取订单号
     * @return
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }
}