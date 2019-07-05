package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 还款流水状态
 */
public enum RepaymentSerialStatusEnum {

    RECHARGED("已充值 ","1"),WAITING_MATCH("待匹配 ","2"),WAITING_RECHARGE("待充值 ","3"),LIKE_MATCH("近似匹配 ","4");

    private String name;

    private String type;


    RepaymentSerialStatusEnum(String name, String type) {
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
