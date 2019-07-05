package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by jinxin on 2017/9/11.
 */
@ApiModel
public class UserInfoVo {
    @ApiModelProperty("报告编号")
    private String reportNo;
    @ApiModelProperty("报告时间")
    private String reportTime;
    @ApiModelProperty("被调人姓名")
    private String userName;
    @ApiModelProperty("省份证号")
    private String idCard;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("银行卡号")
    private String bankCard;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
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

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
}
