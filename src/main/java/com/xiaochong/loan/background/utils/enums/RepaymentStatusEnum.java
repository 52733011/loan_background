package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/9/8.
 */
public enum RepaymentStatusEnum {

    WAITING_REPAYMENT("待还","1"),AMORTIZED_REPAYMENT("已还","2"),OVERDUE_REPAYMENT("逾期待还","3");

    private String name;

    private String type;

    RepaymentStatusEnum(String name, String type) {
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
