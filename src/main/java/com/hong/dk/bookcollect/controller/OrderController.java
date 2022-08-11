package com.hong.dk.bookcollect.controller;


import com.hong.dk.bookcollect.entity.annotation.OptLog;
import com.hong.dk.bookcollect.entity.annotation.TokenToUser;
import com.hong.dk.bookcollect.entity.pojo.UserToken;
import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

import static com.hong.dk.bookcollect.constant.OptTypeConst.SAVE;


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
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "生成订单信息")
    @OptLog(optType = SAVE)
    @PostMapping("/saveOrder")
    public Result<?> saveOrder(@ApiParam("书籍id集合") @RequestParam String bookIdArray, @TokenToUser UserToken user) {
        return Result.ok(orderService.saveOrder(bookIdArray,user.getUserId()));
    }

    @ApiOperation(value = "根据orderId获取订单信息")
    @GetMapping("/getOrderById")
    public Result<?> getOrderById(@ApiParam("订单id") @RequestParam @Size(max = 17,min = 17 ,message = "订单号长度必须为17位") String orderId) {
        return Result.ok(orderService.getOrderById(orderId));
    }

    @ApiOperation(value = "获取所有订单信息")
    @GetMapping("/getAllOrder")
    public Result<?> getAllOrder(@TokenToUser UserToken user) {
        return Result.ok(orderService.getAllOrder(user.getUserId()));
    }

    @ApiOperation(value = "根据status获取订单信息")
    @GetMapping("/getOrderByUserId")
    public Result<?> getOrderByStatus(Integer status){
        return Result.ok(orderService.getOrderByStatus(status));
    }
}

