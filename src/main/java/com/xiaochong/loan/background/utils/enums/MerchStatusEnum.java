package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 商户状态
 */
public enum MerchStatusEnum {

    USING("启用","1"),STOP("停用","0");

    private String name;

    private String type;


    MerchStatusEnum(String name, String type) {
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
