package com.hong.dk.bookcollect.entity.pojo.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@Data
public class CanalDataDTO<T> {

    private String type; // 类型 比如update、insert、delete
    private String table; // 表名
    private List<T> data; //
    private String database; // 数据库名称
    private Long es; // 时间戳
    private Integer id; // id
    private Boolean isDdl; // 是否是DDL操作
    private List<T> old; // 原始数据
    private List<String> pkNames; // 主键名称
    private String sql; // sql语句
    private Long ts; // 时间戳
}
