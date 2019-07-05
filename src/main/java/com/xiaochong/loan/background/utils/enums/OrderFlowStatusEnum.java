package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/5/24.
 */
public enum OrderFlowStatusEnum {
    UNAUTH("未授权","0"),AUTH("认证中","1"),SUCCESS("认证成功","2"),FAIL("认证失败","3");

    private String name;

    private String type;


    OrderFlowStatusEnum(String name, String type) {
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

    @Override
    public String toString() {
        return "OrderFlowStatusEnum{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
