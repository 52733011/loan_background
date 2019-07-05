package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 借款人记录源
 */
public enum BorrowerAccountRecordSourceTypeEnum {

    XIAOCHONG("小虫 ","1"),COMMERCIAL_TENANT("商户 ","0"),SYSTEM_AUTO(" 系统 ","2");

    private String name;

    private String type;


    BorrowerAccountRecordSourceTypeEnum(String name, String type) {
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
