package com.xiaochong.loan.background.utils.enums;

public enum DepositTypeEnum {
    PERCENTAGE(0,"百分比"),ACTUAL(1,"实际金额");


    DepositTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;

    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DepositTypeEnum{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
