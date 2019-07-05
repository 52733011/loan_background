package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/15.
 */
public enum RechargeTypeEnum {

    PREPAYMENT_RECHARGE("预付费充值","1"),POST_PAID_RECHARGE("后付费充值","2"),TRY_OUT_RECHARGE("试用充值","3"),OTHER_RECHARGE("其它","4");

    private String name;

    private String type;

    RechargeTypeEnum(String name, String type) {
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
