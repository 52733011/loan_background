package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchantRechargeVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "商户id")
    private Integer merchId;
    @ApiModelProperty(value = "商户名称")
    private String merchName;
    @ApiModelProperty(value = "充值次数")
    private Integer count;
    @ApiModelProperty(value = "状态1 成功   0 失败")
    private String status;
    @ApiModelProperty(value = "充值原因（仅在变动类型为充值时有效）")
    private String rechargeType;
    @ApiModelProperty(value = "需要认证信息")
    private String rechargeMark;
    @ApiModelProperty(value = "充值凭证名称")
    private String voucherName;
    @ApiModelProperty(value = "充值凭证地址")
    private String voucherUrl;
    @ApiModelProperty(value = "充值时间")
    private String rechargeTime;
    @ApiModelProperty(value = "操作人")
    private Integer rechargeBy;
    @ApiModelProperty(value = "操作人姓名")
    private String rechargeByName;
    @ApiModelProperty(value = "充值编号")
    private String rechargeNo;

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getRechargeByName() {
        return rechargeByName;
    }

    public void setRechargeByName(String rechargeByName) {
        this.rechargeByName = rechargeByName;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getRechargeMark() {
        return rechargeMark;
    }

    public void setRechargeMark(String rechargeMark) {
        this.rechargeMark = rechargeMark;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherUrl() {
        return voucherUrl;
    }

    public void setVoucherUrl(String voucherUrl) {
        this.voucherUrl = voucherUrl;
    }

    public String getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(String rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Integer getRechargeBy() {
        return rechargeBy;
    }

    public void setRechargeBy(Integer rechargeBy) {
        this.rechargeBy = rechargeBy;
    }
}