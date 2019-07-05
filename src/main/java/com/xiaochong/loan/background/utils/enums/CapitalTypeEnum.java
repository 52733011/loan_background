package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 账户记录
 */
public enum CapitalTypeEnum {

    RECHARGE("充值 ","1"),REPAYMENT("还款 ","2");

    private String name;

    private String type;


    CapitalTypeEnum(String name, String type) {
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
