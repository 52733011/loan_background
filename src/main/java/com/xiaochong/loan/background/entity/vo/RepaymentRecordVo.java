package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by jinxin on 2017/10/17.
 */
@ApiModel
public class RepaymentRecordVo {
    @ApiModelProperty("全部待还笔数")
    private  Integer allPendingrePaymentNum;
    @ApiModelProperty("全部待还金额")
    private BigDecimal allPendingRepaymentMoney;
    @ApiModelProperty("逾期待还笔数")
    private  Integer overdueRepaymentNum;
    @ApiModelProperty("逾期待还金额")
    private BigDecimal overdueRepaymentMoney;
    @ApiModelProperty("借款人姓名")
    private String borrowerName;
    @ApiModelProperty("借款人身份证")
    private String borrowerIdCard;
    @ApiModelProperty("借款人手机号")
    private String borrowerPhone;
    @ApiModelProperty("借款人账户余额")
    private BigDecimal borrowerBanlance;
    @ApiModelProperty("还款记录")
    BasePageInfoVo<RepaymentPlanVo> basePageInfoVo;

    public Integer getAllPendingrePaymentNum() {
        return allPendingrePaymentNum;
    }

    public void setAllPendingrePaymentNum(Integer allPendingrePaymentNum) {
        this.allPendingrePaymentNum = allPendingrePaymentNum;
    }

    public BigDecimal getAllPendingRepaymentMoney() {
        return allPendingRepaymentMoney;
    }

    public void setAllPendingRepaymentMoney(BigDecimal allPendingRepaymentMoney) {
        this.allPendingRepaymentMoney = allPendingRepaymentMoney;
    }

    public Integer getOverdueRepaymentNum() {
        return overdueRepaymentNum;
    }

    public void setOverdueRepaymentNum(Integer overdueRepaymentNum) {
        this.overdueRepaymentNum = overdueRepaymentNum;
    }

    public BigDecimal getOverdueRepaymentMoney() {
        return overdueRepaymentMoney;
    }

    public void setOverdueRepaymentMoney(BigDecimal overdueRepaymentMoney) {
        this.overdueRepaymentMoney = overdueRepaymentMoney;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerIdCard() {
        return borrowerIdCard;
    }

    public void setBorrowerIdCard(String borrowerIdCard) {
        this.borrowerIdCard = borrowerIdCard;
    }

    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    public BigDecimal getBorrowerBanlance() {
        return borrowerBanlance;
    }

    public void setBorrowerBanlance(BigDecimal borrowerBanlance) {
        this.borrowerBanlance = borrowerBanlance;
    }

    public BasePageInfoVo<RepaymentPlanVo> getBasePageInfoVo() {
        return basePageInfoVo;
    }

    public void setBasePageInfoVo(BasePageInfoVo<RepaymentPlanVo> basePageInfoVo) {
        this.basePageInfoVo = basePageInfoVo;
    }

    @Override
    public String toString() {
        return "RepaymentRecordVo{" +
                "allPendingrePaymentNum=" + allPendingrePaymentNum +
                ", allPendingRepaymentMoney=" + allPendingRepaymentMoney +
                ", overdueRepaymentNum=" + overdueRepaymentNum +
                ", overdueRepaymentMoney=" + overdueRepaymentMoney +
                ", borrowerName='" + borrowerName + '\'' +
                ", borrowerIdCard='" + borrowerIdCard + '\'' +
                ", borrowerPhone='" + borrowerPhone + '\'' +
                ", borrowerBanlance=" + borrowerBanlance +
                ", basePageInfoVo=" + basePageInfoVo +
                '}';
    }
}
