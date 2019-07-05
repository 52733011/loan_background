package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/14.
 */
public enum LoanApplicationAuditStatusEnum {
    AUDIT_PASS("确认放款","1"),SUBMIT_AGAIN("重新提交","0");

    private String name;

    private String type;


    LoanApplicationAuditStatusEnum(String name, String type) {
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
