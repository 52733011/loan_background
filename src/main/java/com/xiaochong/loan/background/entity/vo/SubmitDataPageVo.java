package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jinxin on 2017/9/5.
 */
@ApiModel
public class SubmitDataPageVo {
    @ApiModelProperty("项目集合")
    private List<MerchDataTemplateProjectVo> merchDataTemplateProjectVoList;
    //个人信息
    @ApiModelProperty("借款人姓名")
    private  String borrowerName;
    @ApiModelProperty("身份证号")
    private  String borrowerIdCard;
    @ApiModelProperty("手机")
    private  String borrowerPhone;
    @ApiModelProperty("银行卡号")
    private  String borrowerBankCard;
    @ApiModelProperty("开户行")
    private  String borrowerBankName;
    @ApiModelProperty("借款金额")
    private BigDecimal loanMoney;
    @ApiModelProperty("分期方式")
    private String stageType;
    @ApiModelProperty("分期信息")
    private  String stageLimit;
    @ApiModelProperty("打款金额")
    private  BigDecimal remitMoney;

    //备注填写人
    @ApiModelProperty("录入人id")
    private  Integer submitBy;
    @ApiModelProperty("录入人姓名")
    private  String submitByName;
    @ApiModelProperty("录入时间")
    private  String submitTime;
    @ApiModelProperty("审核人id")
    private  Integer auditBy;
    @ApiModelProperty("审核人姓名")
    private  String auditByName;
    @ApiModelProperty("审核时间")
    private  String auditTime;
    @ApiModelProperty("备注")
    private String mark;
    @ApiModelProperty("审核内容")
    private String auditContent;
    @ApiModelProperty("放款时间")
    private String loanTime;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("是否是旧订单")
    private String isold;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIsold() {
        return isold;
    }

    public void setIsold(String isold) {
        this.isold = isold;
    }

    public BigDecimal getRemitMoney() {
        return remitMoney;
    }

    public void setRemitMoney(BigDecimal remitMoney) {
        this.remitMoney = remitMoney;
    }

    public String getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public Integer getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(Integer submitBy) {
        this.submitBy = submitBy;
    }

    public String getSubmitByName() {
        return submitByName;
    }

    public void setSubmitByName(String submitByName) {
        this.submitByName = submitByName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(Integer auditBy) {
        this.auditBy = auditBy;
    }

    public String getAuditByName() {
        return auditByName;
    }

    public void setAuditByName(String auditByName) {
        this.auditByName = auditByName;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public List<MerchDataTemplateProjectVo> getMerchDataTemplateProjectVoList() {
        return merchDataTemplateProjectVoList;
    }

    public void setMerchDataTemplateProjectVoList(List<MerchDataTemplateProjectVo> merchDataTemplateProjectVoList) {
        this.merchDataTemplateProjectVoList = merchDataTemplateProjectVoList;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public String getBorrowerBankCard() {
        return borrowerBankCard;
    }

    public void setBorrowerBankCard(String borrowerBankCard) {
        this.borrowerBankCard = borrowerBankCard;
    }

    public String getBorrowerBankName() {
        return borrowerBankName;
    }

    public void setBorrowerBankName(String borrowerBankName) {
        this.borrowerBankName = borrowerBankName;
    }

    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getStageLimit() {
        return stageLimit;
    }

    public void setStageLimit(String stageLimit) {
        this.stageLimit = stageLimit;
    }
}
