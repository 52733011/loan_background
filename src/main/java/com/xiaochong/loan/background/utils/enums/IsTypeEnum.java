package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/9/4.
 *
 */
public enum IsTypeEnum {

    FALSE("否","0"),TRUE("是","1");

    private String name;

    private String type;


    IsTypeEnum(String name, String type) {
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
