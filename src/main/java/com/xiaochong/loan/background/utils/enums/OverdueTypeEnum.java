package com.xiaochong.loan.background.utils.enums;

public enum OverdueTypeEnum {
    CURRENT(0,"当期待还"),ALL(1,"全部待还");


    OverdueTypeEnum(Integer type, String name) {
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
        return "OverdueTypeEnum{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
