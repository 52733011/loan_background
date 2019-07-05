package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ManageLoanApplicationVo {
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("姓名")
    private String userName;
    @ApiModelProperty("身份证")
    private String idCard;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("开户行")
    private String depositBank ;
    @ApiModelProperty("银行卡号")
    private String bankCard;
    @ApiModelProperty("借款金额")
    private String loanMoney;
    @ApiModelProperty("放款金额")
    private String remitMoney;
    @ApiModelProperty("分期方式")
    private String stageType;
    @ApiModelProperty("分期期限")
    private String stageLimit;
    @ApiModelProperty("提交方式")
    private String submitType;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("状态")
    private Integer merchId;
    @ApiModelProperty("状态")
    private String merchName;
    @ApiModelProperty("提交人姓名")
    private String createByName;
    @ApiModelProperty("提交人电话")
    private String createByPhone;
    @ApiModelProperty("创建时间")
    private String createTime;

    public String getStageLimit() {
        return stageLimit;
    }

    public void setStageLimit(String stageLimit) {
        this.stageLimit = stageLimit;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCreateByPhone() {
        return createByPhone;
    }

    public void setCreateByPhone(String createByPhone) {
        this.createByPhone = createByPhone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(String loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getRemitMoney() {
        return remitMoney;
    }

    public void setRemitMoney(String remitMoney) {
        this.remitMoney = remitMoney;
    }

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }


    public String getSubmitType() {
        return submitType;
    }

    public void setSubmitType(String submitType) {
        this.submitType = submitType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}