package com.hong.dk.bookcollect.controller;


import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wqh
 * @since 2022-07-21
 */
@Api(tags = "订单信息接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "生成订单信息")
    @PostMapping("/saveOrder")
    public Result saveOrder(@ApiParam("书籍id集合") @RequestParam String bookIdArray, HttpServletRequest request) {

        return Result.ok(orderService.saveOrder(bookIdArray,request));
    }

    @ApiOperation(value = "根据orderId获取订单信息")
    @GetMapping("/getOrderById")
    public Result getOrderById(@ApiParam("订单id") @RequestParam String orderId) {
        return Result.ok(orderService.getOrderById(orderId));
    }

    @ApiOperation(value = "获取所有订单信息")
    @GetMapping("/getAllOrder")
    public Result getAllOrder(HttpServletRequest request) {
        return Result.ok(orderService.getAllOrder(request));
    }






}

