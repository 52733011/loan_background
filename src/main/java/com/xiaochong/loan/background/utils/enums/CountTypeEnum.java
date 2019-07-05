package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/5/24.
 * 自增编号类型
 */
public enum CountTypeEnum {

    ORDER("订单","01"),REPORT("报告","02");

    private String name;

    private String type;


    CountTypeEnum(String name, String type) {
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
