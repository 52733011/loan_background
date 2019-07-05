package com.xiaochong.loan.background.utils.enums;

public enum OverdueMoneyTypeEnum {
    CAPITAL(0,"本金"),INTEREST_ON_PRINCIPAL(1,"本息");


    OverdueMoneyTypeEnum(Integer type, String name) {
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

    public static String getNameByType(Integer type){
        if(type==null){
            return null;
        }
        switch (type){
            case 0:
                return CAPITAL.getName();
            case 1:
                return INTEREST_ON_PRINCIPAL.getName();
            default:
                return "";
        }
    }
}
