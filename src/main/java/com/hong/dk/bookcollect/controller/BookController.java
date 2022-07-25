package com.hong.dk.bookcollect.controller;


import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 书籍表 前端控制器
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@RestController
@RequestMapping("/book")
@Api(tags = "书籍信息接口")
public class BookController {


    @Autowired
    private BookService bookService;

    @ApiOperation("查询用户未取的书籍")
    @GetMapping("/waitGet")
    public Result bookRetention(HttpServletRequest request) {
        List list= bookService.getBookRetention(request);
        return Result.ok(list);
    }

    @ApiOperation("查询用户已取的书籍")
    @GetMapping("/hasGet")
    public Result bookRetentionHasGet(HttpServletRequest request) {
        List list= bookService.getBookRetentionHasGet(request);
        return Result.ok(list);
    }

    @ApiOperation("查询正在审批的书籍")
    @GetMapping("/approving")
    public Result bookApproving(HttpServletRequest request) {
        List list= bookService.getBookApproving(request);
        return Result.ok(list);
    }

    @ApiOperation("查询所有书籍")
    @GetMapping("/getAllBook")
    public Result getAllBook(HttpServletRequest request) {
        List list= bookService.getAllBook( request);
        return Result.ok(list);
    }


}

