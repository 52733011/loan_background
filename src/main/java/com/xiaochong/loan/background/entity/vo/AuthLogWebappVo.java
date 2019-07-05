package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.AuthLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AuthLogWebappVo {
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "订单号")
    private String orderNum;
    @ApiModelProperty(value = "返回内容")
    private String data;
    @ApiModelProperty(value = "错误信息")
    private String errorMessage;
    @ApiModelProperty(value = "认证类型:运营商认证002,学信网认证005,芝麻认证007",example = "007")
    private String type;
    @ApiModelProperty(value = "认证结果:success,error",example = "success")
    private String result;


    public void setAuthLog(AuthLog authLog) {
        this.idCard = authLog.getIdCard();
        this.orderNum = authLog.getOrderNum();
        this.data = authLog.getData();
        this.errorMessage = authLog.getErrorMessage();
        this.type = authLog.getType();
        this.result = authLog.getResult();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
