package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FlowOptionVo {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "认证流程号")
    private String flowNo;

    @ApiModelProperty(value = "认证流程项号")
    private String flowOptionNo;
    @ApiModelProperty(value = "认证流程项名称")
    private String optionName;
    @ApiModelProperty(value = "认证流程项代码")
    private String optionDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionDesc() {
        return optionDesc;
    }

    public void setOptionDesc(String optionDesc) {
        this.optionDesc = optionDesc;
    }
}