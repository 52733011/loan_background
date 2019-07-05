package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchantinfoFlowVo {

	/** 商户id */
    @ApiModelProperty(value = "商户id")
	private Integer merchId;
	/** 认证流程（checkflow表的id） */
    @ApiModelProperty(value = "认证流程（checkflow表的flowNo）")
	private String flow;

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }
}
