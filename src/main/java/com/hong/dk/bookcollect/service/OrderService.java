package com.hong.dk.bookcollect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hong.dk.bookcollect.entity.pojo.Order;
import com.hong.dk.bookcollect.entity.pojo.vo.OrderVO;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wqh
 * @since 2022-07-21
 */
public interface OrderService extends IService<Order> {

    /**
     * 生成订单信息
     * @param bookIdArray
     * @return
     */
    Map<String,Object> saveOrder(String bookIdArray, String userId);

    OrderVO getOrderById(String orderId);

    List<OrderVO> getAllOrder(String userId);
}
