package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by jinxin on 2017/10/18.
 */
@ApiModel
public class UrgeOverDueSearchVo {
    @ApiModelProperty("总数量")
    private Integer allCount;
    @ApiModelProperty("今日已跟进")
    private Integer urgeedToday;
    @ApiModelProperty("待跟进")
    private Integer waitingUrge;
    @ApiModelProperty("已完成")
    private Integer offTheStocks;
    @ApiModelProperty("放款订单id")
    private Integer applicationId;
    @ApiModelProperty("还款流水记录")
    private BasePageInfoVo<UrgeOverdueVo> basePageInfoVo;

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getUrgeedToday() {
        return urgeedToday;
    }

    public void setUrgeedToday(Integer urgeedToday) {
        this.urgeedToday = urgeedToday;
    }

    public Integer getWaitingUrge() {
        return waitingUrge;
    }

    public void setWaitingUrge(Integer waitingUrge) {
        this.waitingUrge = waitingUrge;
    }

    public Integer getOffTheStocks() {
        return offTheStocks;
    }

    public void setOffTheStocks(Integer offTheStocks) {
        this.offTheStocks = offTheStocks;
    }

    public BasePageInfoVo<UrgeOverdueVo> getBasePageInfoVo() {
        return basePageInfoVo;
    }

    public void setBasePageInfoVo(BasePageInfoVo<UrgeOverdueVo> basePageInfoVo) {
        this.basePageInfoVo = basePageInfoVo;
    }
}
