package com.hong.dk.bookcollect.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.entity.pojo.Order;
import com.hong.dk.bookcollect.entity.pojo.OrderBook;
import com.hong.dk.bookcollect.entity.pojo.vo.OrderVO;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.BookMapper;
import com.hong.dk.bookcollect.mapper.OrderBookMapper;
import com.hong.dk.bookcollect.mapper.OrderMapper;
import com.hong.dk.bookcollect.result.enmu.BookStatusEnum;
import com.hong.dk.bookcollect.result.enmu.OrderStatusEnum;
import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import com.hong.dk.bookcollect.service.OrderService;
import com.hong.dk.bookcollect.utils.OrderUtil;
import com.hong.dk.bookcollect.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wqh
 * @since 2022-07-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private OrderBookMapper orderBookMapper;
    @Autowired
    private BookMapper bookMapper;
    


    @Override
    @Transactional
    public Map<String,Object> saveOrder(String bookIdArray, String userId) {
        Map<String,Object> map = new HashMap<>();

        //新建一个Order对象
        Order order = new Order();
        order.setUserId(userId.trim()); //设置userid
        order.setStatus(OrderStatusEnum.WAITING_GET.getOrderStatus()); //设置状态为0，即待审批
        order.setPickTime(LocalDateTime.now()); //设置取书时间为当前时间
        order.setOrderId(OrderUtil.getOrderNo().trim()); //生成订单编号
        int i = baseMapper.insert(order);//插入数据库
        if (i > 0) {
            //插入成功，获取订单id
            String orderId = order.getOrderId();
            //获取书籍id数组
            String[] bookIds = bookIdArray.split(",");
            //判断order_book表是否存在这些书籍
            List<OrderBook> books = orderBookMapper.selectList(Wrappers.<OrderBook>lambdaQuery().in(OrderBook::getBookId, bookIds));
            if (books.size() > 0) {
                Asserts.fail("书籍重复提交",201);
            }
            for (String bookId : bookIds) {
                OrderBook orderBook = new OrderBook();//新建一个OrderBook对象
                orderBook.setOrderId(orderId);
                orderBook.setBookId(bookId);
                int flag1 = orderBookMapper.insert(orderBook);//插入数据库
                Book book = new Book();
                book.setId(bookId);
                book.setPickStatus(BookStatusEnum.UNDER_APPROVAL.getBookStatus());  //设置书籍状态审批中
                int flag2 = bookMapper.updateById(book);  //更新书籍状态为1，已借出
                if (flag1 <= 0 || flag2 <= 0) {
                    Asserts.fail("插入失败", 201);
                }
            }
            String key = userId+":book:*";
            redisTemplate.delete(key);


            OrderVO orderVO = Optional.of(order).map(OrderVO::new).orElse(null); //map将order转换为orderVO
            map.put("msg","提交成功");
            map.put("order",orderVO);
            return map;
        }
        Asserts.fail(ResultCodeEnum.FAIL);
        return null;
    }

    @Override
    public OrderVO getOrderById(String orderId) {
        //先查询redis中是否存在
        OrderVO orderVO_Redis = JSON.parseObject(redisTemplate.opsForValue().get(UserUtil.getUserId()+":order:"+orderId), OrderVO.class);
        if (null != orderVO_Redis) {
            return orderVO_Redis;
        }
        //根据订单id查询订单信息
        Order order = baseMapper.selectById(orderId);
        if(order == null){
            Asserts.fail("订单不存在",201);
        }
        OrderVO orderVO = Optional.of(order).map(OrderVO::new).orElse(null); //map将order转换为orderVO
        Optional.of(orderVO).ifPresent(this::addBookList);
        //存入redis中
        redisTemplate.opsForValue().set(order.getUserId()+":order:"+orderId, JSON.toJSONString(orderVO),60, TimeUnit.SECONDS);
        return orderVO;
    }

    @Override
    public List<OrderVO> getAllOrder(String userId) {

        //根据userid查询所有订单信息
        LambdaQueryWrapper<Order> lambdaQueryWrapper = Wrappers.lambdaQuery(Order.class).eq(Order::getUserId,userId);
        List<Order> orderList = baseMapper.selectList(lambdaQueryWrapper);
        return Optional.ofNullable(orderList) //map将orderList转换为orderVOList
                                            .map(list -> list.stream().map(OrderVO::new)
                                                    .collect(Collectors.toList()))
                                            .orElse(null);
    }


    /**
     * 补充书籍列表信息
     */
    private void addBookList(OrderVO orderVO){
        //根据订单id查询bookId
        LambdaQueryWrapper<OrderBook> lambdaQueryWrapper = new LambdaQueryWrapper<OrderBook>()
                .select(OrderBook::getBookId)
                .eq(OrderBook::getOrderId,orderVO.getOrderId());
        List<Integer> bookIds = orderBookMapper.selectObjs(lambdaQueryWrapper).stream()
                .map(o -> (Integer) o)  //selectObjs方法是查询出来的是一个list<Object>类型，需要转换成list<Integer>类型
                .collect(Collectors.toList());
//        System.out.println("bookIds:" + bookIds);
        List<Book> bookList = bookMapper.selectList(Wrappers.lambdaQuery(Book.class).in(Book::getId,bookIds));
        Optional.ofNullable(bookList).ifPresent(orderVO::setBookList);
    }

}
