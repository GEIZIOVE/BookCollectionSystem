package com.hong.dk.bookcollect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.entity.pojo.Book;
import com.hong.dk.bookcollect.entity.pojo.Order;
import com.hong.dk.bookcollect.entity.pojo.OrderBook;
import com.hong.dk.bookcollect.entity.pojo.vo.OrderVO;
import com.hong.dk.bookcollect.handler.Asserts;
import com.hong.dk.bookcollect.mapper.BookMapper;
import com.hong.dk.bookcollect.mapper.OrderBookMapper;
import com.hong.dk.bookcollect.mapper.OrderMapper;
import com.hong.dk.bookcollect.service.BookService;
import com.hong.dk.bookcollect.service.OrderService;
import com.hong.dk.bookcollect.utils.OrderUtil;
import com.hong.dk.bookcollect.utils.TokenUtil;

import com.hong.dk.bookcollect.utils.helper.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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
    OrderBookMapper orderBookMapper;

    @Autowired
    BookMapper bookMapper;

    @Override
    @Transactional
    public Map<String,Object> saveOrder(String bookIdArray, HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);

        //新建一个Order对象
        Order order = new Order();
        order.setUserId(userId.trim()); //设置userid
        order.setOrderId(OrderUtil.getOrderNo().trim()); //设置订单编号
        int i = this.baseMapper.insert(order);//插入数据库
        if (i > 0) {
            //插入成功，获取订单id
            String orderId = order.getOrderId();
            //获取书籍id数组
            String[] bookIds = bookIdArray.split(",");
//            System.out.println("bookIds:" + bookIds);
            for (String bookId : bookIds) {
                OrderBook orderBook = new OrderBook();//新建一个OrderBook对象
                orderBook.setOrderId(orderId);//设置订单id
                orderBook.setBookId(Integer.parseInt(bookId));//设置书籍id
                int flag1 = orderBookMapper.insert(orderBook);//插入数据库
                Book book = new Book();
                book.setId(Integer.parseInt(bookId));
                book.setPickStatus(2);  //设置书籍状态审批中
                int flag2 = bookMapper.updateById(book);  //更新书籍状态为1，已借出
                if (flag1 <= 0 && flag2 <= 0) {
                    Asserts.fail("插入失败", 201);
                }
            }
            //删除redis中的相关缓存
            redisTemplate.delete(  userId +":"+"retentionBookHasGet");
            redisTemplate.delete( userId  +":"+ "retention0Book");
            redisTemplate.delete( userId  +":"+ "allBook");
            redisTemplate.delete( userId  +":"+ "approvingBook");

            OrderVO orderVO = new OrderVO();
            orderVO.setOrderId(orderId);
            orderVO.setUserId(userId);
            orderVO.setStatus(0);//设置订单状态为0，未审批
            orderVO.setPickTime(order.getPickTime());//设置订单提交时间时间
            map.put("msg","提交成功");
            map.put("order",orderVO);
            return map;
        }
        Asserts.fail("插入失败", 201);
        return null;
    }

    @Override
    public OrderVO getOrderById(String orderId) {

        //根据订单id查询订单信息
        Order order = this.baseMapper.selectById(orderId);
        if(order == null){
            Asserts.fail("订单不存在",201);
        }
        OrderVO orderVO = Optional.ofNullable(order).map(OrderVO::new).orElse(null);
//        orderVO.setOrderId(order.getOrderId());
//        orderVO.setUserId(order.getUserId());
//        orderVO.setStatus(order.getStatus());
//        orderVO.setPickTime(order.getPickTime());

        //根据订单id查询bookId
        LambdaQueryWrapper<OrderBook> lambdaQueryWrapper = new LambdaQueryWrapper<OrderBook>()
                .select(OrderBook::getBookId)
                .eq(OrderBook::getOrderId,orderId);
        List<Integer> bookIds = orderBookMapper.selectObjs(lambdaQueryWrapper).stream()
                .map(o -> (Integer) o)  //selectObjs方法是查询出来的是一个list<Object>类型，需要转换成list<Integer>类型
                .collect(Collectors.toList());
//        System.out.println("bookIds:" + bookIds);
        List<Book> bookList = bookMapper.selectBatchIds(bookIds);
        orderVO.setBookList(bookList);
//        System.out.println("bookList:" + bookList);
        return orderVO;
    }

    @Override
    public List<OrderVO> getAllOrder(HttpServletRequest request) {
        //获取token
        String token = TokenUtil.getToken(request);
        //获取userid
        String userId = JwtHelper.getUserId(token);
        //根据userid查询所有订单信息
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Order::getUserId,userId);
        List<Order> orderList = this.baseMapper.selectList(lambdaQueryWrapper);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderId(order.getOrderId());
            orderVO.setUserId(order.getUserId());
            orderVO.setStatus(order.getStatus());
            orderVO.setPickTime(order.getPickTime());
        }
        return orderVOList;
    }

}
