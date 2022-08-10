 package com.hong.dk.bookcollect.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.mapper.BookMapper;
import com.hong.dk.bookcollect.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 书籍表 服务实现类
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public List<Book> getAllBook(String userId) {
        //根据userid查询redis
        List<Book> list = JSON.parseArray(redisTemplate.opsForValue().get(userId+":book:"+ "allBook"), Book.class);
        if (list != null) {
            return list;
        }

        List<Book> books =  baseMapper.selectList(Wrappers.lambdaQuery(Book.class).eq(Book::getUserId, userId));
        //将书籍存入redis
        if (books != null && books.size() > 0) {
            redisTemplate.opsForValue().set(userId +":book:"+ "allBook", JSON.toJSONString(books), 20, TimeUnit.SECONDS);
        }
        return books;
    }

    @Override
    public List<Book> getBookList(Integer bookStatus, String userId) {
        //根据userId查询redis
        List<Book> list = JSON.parseArray(redisTemplate.opsForValue().get(userId+":book:"+ bookStatus), Book.class);
        if (null != list){
            return list;
        }
        List<Book> books = baseMapper.selectList(Wrappers.lambdaQuery(Book.class).eq(Book::getUserId, userId).eq(Book::getPickStatus, bookStatus));
        //将书籍存入redis
        if (books != null && books.size() > 0) {
            redisTemplate.opsForValue().set(userId +":book:"+ bookStatus, JSON.toJSONString(books), 20, TimeUnit.SECONDS);
        }
        return books;
    }
}
