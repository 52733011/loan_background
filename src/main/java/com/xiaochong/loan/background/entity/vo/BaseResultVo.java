package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BaseResultVo<T> {

    @ApiModelProperty(value = "状态码：0表示成功，500表示系统错误，401表示缺少参数",example = "0")
    private String code;

    @ApiModelProperty(value = "错误信息描述",example = "操作成功")
    private String message;

    @ApiModelProperty(value = "业务信息封装类")
    private BusinessVo<T> result;


    @ApiModelProperty(value = "时间戳")
    private Long currentDate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessVo<T> getResult() {
        return result;
    }

    public void setResult(BusinessVo<T> result) {
        this.result = result;
    }

    public Long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Long currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public String toString() {
        return "BaseResultVo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", currentDate=" + currentDate +
                '}';
    }
}
