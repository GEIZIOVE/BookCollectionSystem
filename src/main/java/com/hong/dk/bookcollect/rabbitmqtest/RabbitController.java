//package com.hong.dk.bookcollect.rabbitmqtest;
//
//import com.hong.dk.bookcollect.result.Result;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Api(tags = "RabbitController", description = "RabbitMQ功能测试")
//@Controller
//@RequestMapping("/rabbit")
//public class RabbitController {
//
//    @Autowired
//    private com.hong.dk.bookcollect.test.SimpleSender simpleSender;
//
//    @ApiOperation("简单模式")
//    @RequestMapping(value = "/simple", method = RequestMethod.GET)
//    @ResponseBody
//    public Result simpleTest() throws InterruptedException {
//        for(int i=0;i<10;i++){
//            simpleSender.send();
//            Thread.sleep(1000);
//        }
//        return Result.ok();
//    }
//}