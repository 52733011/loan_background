package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/5/24.
 * 账户变动类型
 */
public enum TransactTypeEnum {

    LOCK("锁定","0"),EXPEND("支出","1"),RETURN("返还","2"),RECHARGE("充值","3");

    private String name;

    private String type;


    TransactTypeEnum(String name, String type) {
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
