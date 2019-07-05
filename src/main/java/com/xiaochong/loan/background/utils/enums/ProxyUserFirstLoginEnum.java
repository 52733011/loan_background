package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/8/9.
 */
public enum ProxyUserFirstLoginEnum {

    YES("初次登录","1"),NO("非初次登录","0");

    private String name;

    private String type;


    ProxyUserFirstLoginEnum(String name, String type) {
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
