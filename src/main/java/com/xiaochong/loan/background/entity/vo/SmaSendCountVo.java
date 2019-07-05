package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


@ApiModel
public class SmaSendCountVo {
    @ApiModelProperty(value = "公司id")
    private Integer merchId;
    @ApiModelProperty(value = "当月发送次数")
    private Long monthCount;
    @ApiModelProperty(value = "当月发送价格")
    private BigDecimal monthPrice;
    @ApiModelProperty(value = "所有发送次数")
    private Long allCount;
    @ApiModelProperty(value = "所有发送价格")
    private BigDecimal allPrice;

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public Long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Long monthCount) {
        this.monthCount = monthCount;
    }

    public BigDecimal getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(BigDecimal monthPrice) {
        this.monthPrice = monthPrice;
    }

    public Long getAllCount() {
        return allCount;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }
}
