package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OrderManageVo {
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "姓名")
    private String realname;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "发起时间")
    private String createtime;
    @ApiModelProperty(value = "状态：0未授权，1已授权，2已支付")
    private String status;
    @ApiModelProperty(value = "商户号")
    private Integer merchId;
    @ApiModelProperty(value = "商户名称")
    private String merchName;
    @ApiModelProperty(value = "是否为旧系统订单")
    private String isOld;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIsOld() {
        return isOld;
    }

    public void setIsOld(String isOld) {
        this.isOld = isOld;
    }
}
