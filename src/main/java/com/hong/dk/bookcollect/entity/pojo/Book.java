package com.hong.dk.bookcollect.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hong.dk.bookcollect.entity.pojo.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 书籍表
 * </p>
 *
 * @author wqh
 * @since 2022-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("book")
@ApiModel(value="Book对象", description="书籍表")
public class Book extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "书籍名称")
    private String bookName;

    @ApiModelProperty(value = "期数")
    private String periods;

    @ApiModelProperty(value = "ISBN编码")
    private String isbn;

    @ApiModelProperty(value = "书籍图片url")
    private String bookPhoto;

    @ApiModelProperty(value = "书籍数量")
    private String bookNumber;

    @ApiModelProperty(value = "入库时间")
    private LocalDateTime putTime;

    @ApiModelProperty(value = "取件状态 0表示未取，1表示已取走，2表示审批中，默认未0")
    private Integer pickStatus;

    @ApiModelProperty(value = "学工号")
    private String userId;

}
