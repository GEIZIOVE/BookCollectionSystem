package com.hong.dk.bookcollect.entity.exception;


import com.hong.dk.bookcollect.result.enmu.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

    /**
 * 自定义全局异常类
 *
 * @author wqh
 */
@Data
@ApiModel(value = "自定义全局异常类")
public class BookCollectException extends RuntimeException {

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    private ResultCodeEnum resultCodeEnum;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public BookCollectException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public BookCollectException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
    }

    @Override
    public String toString() {
        return "BookCollectException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
