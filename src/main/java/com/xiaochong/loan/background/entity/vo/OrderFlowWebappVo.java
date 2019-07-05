package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.Checkflow;
import com.xiaochong.loan.background.entity.po.OrderFlow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OrderFlowWebappVo {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "订单token")
    private String orderToken;
    @ApiModelProperty(value = "商户id")
    private Integer merchId;
    @ApiModelProperty(value = "认证状态：0未授权，1认证中，2认证成功，3认证失败",example = "0")
    private String status;
    @ApiModelProperty(value = "认证流程名称")
    private String flowName;
    @ApiModelProperty(value = "流程号")
    private String flowNo;
    @ApiModelProperty(value = "地址")
    private String url;
    @ApiModelProperty(value = "002---运营商认证 005---学信网认证  007---芝麻认证")
    private String type;
    @ApiModelProperty(value = "流程步骤")
    private Integer step;


    public void setOrderFlow(OrderFlow orderFlow) {
        this.orderNo = orderFlow.getOrderNo();
        this.orderToken = orderFlow.getOrderToken();
        this.merchId = orderFlow.getMerchId();
        this.status = orderFlow.getStatus();
    }

    public void setCheckflow(Checkflow checkflow) {
        this.flowName = checkflow.getFlowName();
        this.flowNo = checkflow.getFlowNo();
        this.url = checkflow.getUrl();
        this.type = checkflow.getType();
        this.step = checkflow.getStep();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

}
