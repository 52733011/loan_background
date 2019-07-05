package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 借贷审核类型
 */
public enum LoanAuditTypeEnum {

    MERCHANT_AUDIT("商户审核","1"),XIAOCHONG_AUDIT("小虫审核","0");

    private String name;

    private String type;


    LoanAuditTypeEnum(String name, String type) {
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
