package com.hong.dk.bookcollect.mapper;

import com.hong.dk.bookcollect.entity.pojo.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 书籍表 Mapper 接口
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Repository
public interface BookMapper extends BaseMapper<Book> {

}
