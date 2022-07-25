 package com.hong.dk.bookcollect.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.mapper.BookMapper;
import com.hong.dk.bookcollect.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.utils.TokenUtil;
import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
    public List<Book> getBookRetention(HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);

        //根据userid查询redis
        List<Book> list = JSON.parseArray(redisTemplate.opsForValue().get( userId+":"+"retention0Book"), Book.class);

        if (list != null) {
            return list;
        }
        //根据userid查询用户所待取的书籍
        List<Book> books = (List<Book>) baseMapper.selectList(new QueryWrapper<Book>().eq("user_id", userId).eq("pick_status", 0));
        //将书籍存入redis
        if (books != null && books.size() > 0) {
            redisTemplate.opsForValue().set(userId +":"+ "retention0Book", JSON.toJSONString(books), 20, TimeUnit.SECONDS
            );
        }

        return books;

    }

    @Override
    public List<Book> getBookRetentionHasGet(HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);
        //根据userid查询redis
        List<Book> list = JSON.parseArray(redisTemplate.opsForValue().get( userId +":"+"retentionBookHasGet"), Book.class);
        if (list != null) {
            return list;
        }
        //根据userid查询用户所待取的书籍
        List<Book> books = (List<Book>) baseMapper.selectList(new QueryWrapper<Book>().eq("user_id", userId).eq("pick_status", 1));
        //将书籍存入redis

        if (books != null && books.size() > 0) {
            redisTemplate.opsForValue().set( userId +":"+"retentionBookHasGet", JSON.toJSONString(books), 20, TimeUnit.SECONDS);
        }
        return books;
    }

    @Override
    public List getAllBook(HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);
        //根据userid查询redis
        List<Book> list = JSON.parseArray(redisTemplate.opsForValue().get(userId+":"+ "allBook"), Book.class);
        if (list != null) {
            return list;
        }
        List<Book> books = baseMapper.selectList(new QueryWrapper<Book>().eq("user_id", userId));
        //将书籍存入redis
        if (books != null && books.size() > 0) {
            redisTemplate.opsForValue().set(userId +":"+ "allBook", JSON.toJSONString(books), 20, TimeUnit.SECONDS);
        }
        return books;
    }

    @Override
    public List getBookApproving(HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);
        //根据userid查询redis
        List<Book> list = JSON.parseArray(redisTemplate.opsForValue().get(userId+":"+ "approvingBook"), Book.class);
        if (list != null) {
            return list;
        }

        List<Book> books = baseMapper.selectList(new QueryWrapper<Book>().eq("user_id", userId).eq("pick_status", 2));
        //将书籍存入redis
        if (books != null && books.size() > 0) {
            redisTemplate.opsForValue().set(userId +":"+ "approvingBook", JSON.toJSONString(books), 20, TimeUnit.SECONDS);
        }
        return books;
    }
}
