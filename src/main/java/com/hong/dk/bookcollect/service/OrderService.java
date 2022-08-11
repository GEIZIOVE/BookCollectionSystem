package com.hong.dk.bookcollect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hong.dk.bookcollect.entity.pojo.Order;
import com.hong.dk.bookcollect.entity.pojo.vo.OrderInformationVO;
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
     * @return map集合
     */
    Map<String,Object> saveOrder(String bookIdArray, String userId);

    OrderVO getOrderById(String orderId);

    List<OrderVO> getAllOrder(String userId);

    List<OrderInformationVO> getOrderByStatus(Integer status);
}
