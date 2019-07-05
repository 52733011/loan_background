package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/14.
 */
public enum MerchDataTemplateStatusEnum {
    USING("可用","1"),FORBIDDEN("禁用","0");

    private String name;

    private String type;


    MerchDataTemplateStatusEnum(String name, String type) {
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
