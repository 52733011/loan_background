package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by jinxin on 2017/8/16.
 */
@ApiModel
public class MerchantAccountVo {
    @ApiModelProperty(value = "商户id")
    private Integer merchId;
    @ApiModelProperty(value = "账户余额")
    private Integer surplusCount;
    @ApiModelProperty(value = "锁定次数")
    private Integer lockCount;
    @ApiModelProperty(value = "账户状态")
    private String accountStatus;

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public Integer getSurplusCount() {
        return surplusCount;
    }

    public void setSurplusCount(Integer surplusCount) {
        this.surplusCount = surplusCount;
    }

    public Integer getLockCount() {
        return lockCount;
    }

    public void setLockCount(Integer lockCount) {
        this.lockCount = lockCount;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
