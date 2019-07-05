package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/8/9.
 */
public enum ProxyUserStatusEnum {

    EFFECTIVE("有效","1"),INVALID("无效","0");

    private String name;

    private String type;


    ProxyUserStatusEnum(String name, String type) {
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
