package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.Checkflow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CheckflowVo {

    @ApiModelProperty(value = "流程主键")
	private String id;
	/** 认证流程名称 */
    @ApiModelProperty(value = "认证流程名称")
	private String flowName;
    @ApiModelProperty(value = "认证流程状态 （备用）")
	private String status;
    @ApiModelProperty(value = "url地址")
	private String url;
    @ApiModelProperty(value = "流程类型 002---运营商认证 005---学信网认证  007---芝麻认证")
	private String type;

	public void setCheckflow(Checkflow checkflow) {
		this.id = checkflow.getId();
		this.flowName = checkflow.getFlowName();
		this.status = checkflow.getStatus();
		this.url = checkflow.getUrl();
		this.type = checkflow.getType();
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    @Override
    public String toString() {
        return "LoanCheckflow{" +
                "id='" + id + '\'' +
                ", flowname='" + flowName + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                '}';
    }


}
