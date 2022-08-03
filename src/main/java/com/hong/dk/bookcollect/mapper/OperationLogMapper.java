package com.hong.dk.bookcollect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hong.dk.bookcollect.entity.pojo.OperationLog;
import org.springframework.stereotype.Repository;


/**
 * 操作日志
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
