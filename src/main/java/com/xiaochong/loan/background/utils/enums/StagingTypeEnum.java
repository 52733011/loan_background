package com.xiaochong.loan.background.utils.enums;

public enum StagingTypeEnum {
    MONTH(0,"按月放款"),DAY(1,"按日放款");


    StagingTypeEnum(Integer type, String name) {
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
        switch (type){
            case 0:
                return MONTH.getName();
            case 1:
                return DAY.getName();
            default:
                return "";
        }
    }
}
