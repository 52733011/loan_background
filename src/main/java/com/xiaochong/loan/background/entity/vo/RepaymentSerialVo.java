package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by jinxin on 2017/10/18.
 */
@ApiModel
public class RepaymentSerialVo {
    @ApiModelProperty("总数量")
    private Integer allCount;
    @ApiModelProperty("待匹配数量")
    private Integer waitMatchCount;
    @ApiModelProperty("代充值数量")
    private Integer waitRechargeCount;
    @ApiModelProperty("已充值数量")
    private Integer rechargeEdCount;
    @ApiModelProperty("还款流水记录")
    private BasePageInfoVo<RepaymentSerialRecordVo> basePageInfoVo;

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getWaitMatchCount() {
        return waitMatchCount;
    }

    public void setWaitMatchCount(Integer waitMatchCount) {
        this.waitMatchCount = waitMatchCount;
    }

    public Integer getWaitRechargeCount() {
        return waitRechargeCount;
    }

    public void setWaitRechargeCount(Integer waitRechargeCount) {
        this.waitRechargeCount = waitRechargeCount;
    }

    public Integer getRechargeEdCount() {
        return rechargeEdCount;
    }

    public void setRechargeEdCount(Integer rechargeEdCount) {
        this.rechargeEdCount = rechargeEdCount;
    }

    public BasePageInfoVo<RepaymentSerialRecordVo> getBasePageInfoVo() {
        return basePageInfoVo;
    }

    public void setBasePageInfoVo(BasePageInfoVo<RepaymentSerialRecordVo> basePageInfoVo) {
        this.basePageInfoVo = basePageInfoVo;
    }
}
