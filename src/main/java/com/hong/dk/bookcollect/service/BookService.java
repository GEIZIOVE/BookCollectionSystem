package com.hong.dk.bookcollect.service;

import com.hong.dk.bookcollect.entity.pojo.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 书籍表 服务类
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
public interface BookService extends IService<Book> {



    List<Book> getAllBook(String userId);
    List<Book> getBookList(Integer bookStatus, String userId);
}
