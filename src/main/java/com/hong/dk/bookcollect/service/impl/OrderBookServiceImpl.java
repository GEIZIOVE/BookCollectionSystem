package com.hong.dk.bookcollect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hong.dk.bookcollect.entity.pojo.OrderBook;
import com.hong.dk.bookcollect.mapper.OrderBookMapper;
import com.hong.dk.bookcollect.service.OrderBookService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wqh
 * @since 2022-07-21
 */
@Service
public class OrderBookServiceImpl extends ServiceImpl<OrderBookMapper, OrderBook> implements OrderBookService {

}
