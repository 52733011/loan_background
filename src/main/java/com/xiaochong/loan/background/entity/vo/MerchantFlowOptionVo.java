package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchantFlowOptionVo {

    @ApiModelProperty(value = "商户id")
    private Integer merchId;

    @ApiModelProperty(value = "流程号")
    private String flowNo;

    @ApiModelProperty(value = "流程选项号")
    private String flowOptionNo;


    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getFlowOptionNo() {
        return flowOptionNo;
    }

    public void setFlowOptionNo(String flowOptionNo) {
        this.flowOptionNo = flowOptionNo;
    }
}