package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
@ApiModel
public class RepaymentSerialRecordVo {
  @ApiModelProperty("还款流水id")
    private Integer id;
    @ApiModelProperty("商户id")
    private Integer merchId;
    @ApiModelProperty("商户名称")
    private String merchName;
    @ApiModelProperty("借款人id")
    private Integer borrowerId;
    @ApiModelProperty("借款人姓名")
    private String borrowerName;
    @ApiModelProperty("借款人手机号")
    private String borrowerIdCard;
    @ApiModelProperty("借款人手机号")
    private String borrowerPhone;
    @ApiModelProperty("交易流水号")
    private String dealSerialNo;
    @ApiModelProperty("账务流水号")
    private String accountingSerialNo;
    @ApiModelProperty("交易时间")
    private String dealTime;
    @ApiModelProperty("转账账户姓名")
    private String transferAccountName;
    @ApiModelProperty("转账金额")
    private BigDecimal transferMoney;
    @ApiModelProperty("转账备注")
    private String transferMark;
    @ApiModelProperty("流水状态 1  已充值 2 待匹配 3 待充值 4 近似匹配")
    private String status;

    public String getBorrowerIdCard() {
        return borrowerIdCard;
    }

    public void setBorrowerIdCard(String borrowerIdCard) {
        this.borrowerIdCard = borrowerIdCard;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    public String getDealSerialNo() {
        return dealSerialNo;
    }

    public void setDealSerialNo(String dealSerialNo) {
        this.dealSerialNo = dealSerialNo;
    }

    public String getAccountingSerialNo() {
        return accountingSerialNo;
    }

    public void setAccountingSerialNo(String accountingSerialNo) {
        this.accountingSerialNo = accountingSerialNo;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getTransferAccountName() {
        return transferAccountName;
    }

    public void setTransferAccountName(String transferAccountName) {
        this.transferAccountName = transferAccountName;
    }

    public BigDecimal getTransferMoney() {
        return transferMoney;
    }

    public void setTransferMoney(BigDecimal transferMoney) {
        this.transferMoney = transferMoney;
    }

    public String getTransferMark() {
        return transferMark;
    }

    public void setTransferMark(String transferMark) {
        this.transferMark = transferMark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RepaymentSerialRecordVo{" +
                "id=" + id +
                ", merchId=" + merchId +
                ", merchName=" + merchName +
                ", borrowerId=" + borrowerId +
                ", borrowerName=" + borrowerName +
                ", borrowerPhone=" + borrowerPhone +
                ", dealSerialNo='" + dealSerialNo + '\'' +
                ", accountingSerialNo='" + accountingSerialNo + '\'' +
                ", dealTime='" + dealTime + '\'' +
                ", transferAccountName='" + transferAccountName + '\'' +
                ", transferMoney=" + transferMoney +
                ", transferMark='" + transferMark + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}