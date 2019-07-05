package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
@ApiModel
public class UrgeOverdueVo {
   @ApiModelProperty("催收逾期放款id")
    private Integer id;
    @ApiModelProperty("放款订单id")
    private Integer applicationId;
    @ApiModelProperty("借款人主键")
    private Integer borrowerId;
    @ApiModelProperty("姓名")
    private String borrowerName;
    @ApiModelProperty("手机号")
    private String borrowerPhone;
    @ApiModelProperty("逾期期数")
    private Integer overdueNum;
    @ApiModelProperty("在逾期总本息")
    private BigDecimal overdueAllMoney;
    @ApiModelProperty("上次更进id")
    private Integer lastFollowId;
    @ApiModelProperty("上次更进姓名")
    private String lastFollowName;
    @ApiModelProperty("上次更进时间")
    private String lastFollowTime;
    @ApiModelProperty("状态 1 今日已跟进 2 待跟进  3  已完成")
    private String status;
    @ApiModelProperty("最长逾期天数")
    private Integer maxOverdueDays;

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getLastFollowName() {
        return lastFollowName;
    }

    public void setLastFollowName(String lastFollowName) {
        this.lastFollowName = lastFollowName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(Integer overdueNum) {
        this.overdueNum = overdueNum;
    }

    public BigDecimal getOverdueAllMoney() {
        return overdueAllMoney;
    }

    public void setOverdueAllMoney(BigDecimal overdueAllMoney) {
        this.overdueAllMoney = overdueAllMoney;
    }

    public Integer getLastFollowId() {
        return lastFollowId;
    }

    public void setLastFollowId(Integer lastFollowId) {
        this.lastFollowId = lastFollowId;
    }



    public String getLastFollowTime() {
        return lastFollowTime;
    }

    public void setLastFollowTime(String lastFollowTime) {
        this.lastFollowTime = lastFollowTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMaxOverdueDays() {
        return maxOverdueDays;
    }

    public void setMaxOverdueDays(Integer maxOverdueDays) {
        this.maxOverdueDays = maxOverdueDays;
    }
}