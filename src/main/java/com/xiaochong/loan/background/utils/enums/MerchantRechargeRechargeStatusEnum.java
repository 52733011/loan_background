package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 商户状态
 */
public enum MerchantRechargeRechargeStatusEnum {

    SUCCESS("成功","1"),FAILED("失败","0");

    private String name;

    private String type;


    MerchantRechargeRechargeStatusEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
