package com.hong.dk.bookcollect.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hong.dk.bookcollect.entity.pojo.OrderBook;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wqh
 * @since 2022-07-21
 */
@Repository
public interface OrderBookMapper extends BaseMapper<OrderBook> {

}
