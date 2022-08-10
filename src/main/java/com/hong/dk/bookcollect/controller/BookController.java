package com.hong.dk.bookcollect.controller;


import com.hong.dk.bookcollect.entity.annotation.TokenToUser;
import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.entity.pojo.UserToken;
import com.hong.dk.bookcollect.result.Result;
import com.hong.dk.bookcollect.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @ApiOperation("根据书籍状态查询书籍")
    @GetMapping("/listByStatus")
public Result<?> list(@RequestParam @ApiParam(value = "取件状态 0表示未取，1表示已取走，2表示审批中") Integer bookStatus,
                   @TokenToUser UserToken user){
        return Result.ok(bookService.getBookList(bookStatus,user.getUserId()));
    }


    @ApiOperation("查询所有书籍")
    @GetMapping("/getAllBook")
    public Result<?> getAllBook(@TokenToUser UserToken user) {
        List<Book> list= bookService.getAllBook( user.getUserId());
        return Result.ok(list);
    }


}

