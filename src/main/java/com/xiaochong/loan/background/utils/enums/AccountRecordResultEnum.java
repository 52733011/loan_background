package com.xiaochong.loan.background.utils.enums;

/**
 * Created by ray.liu on 2017/5/10.
 */
public enum AccountRecordResultEnum {

    LOCK("0","锁定"),SUCCESS("1","成功"),BACK("2","退回");

    AccountRecordResultEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private String type;

    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
