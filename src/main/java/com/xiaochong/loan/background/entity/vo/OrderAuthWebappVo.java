package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class OrderAuthWebappVo {
    @ApiModelProperty(value = "需要认证信息")
    private List<OrderFlowWebappVo> orderFlowWebappVos;
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    @ApiModelProperty(value = "订单状态0未授权 1认证中 2报告生成中 3已拒绝 4已取消 5报告完成 6已过期 7异常")
    private String orderStatus;

    public List<OrderFlowWebappVo> getOrderFlowWebappVos() {
        return orderFlowWebappVos;
    }

    public void setOrderFlowWebappVos(List<OrderFlowWebappVo> orderFlowWebappVos) {
        this.orderFlowWebappVos = orderFlowWebappVos;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
