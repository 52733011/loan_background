package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/9/8.
 *
 */
public enum SmsSendStatueEnum {
    STAGED_LOAN_SUCCESS("分期放款成功","0"),
    REPAYMENT_SUCCESS("分期还款成功（非最后一期）","1"),
    LAST_REPAYMENT_SUCCESS("分期还款成功（最后一期）","2"),
    NEAR_REPAYMENT("临近还款日","3"),
    OVERDUE("已逾期","4");

    private String name;

    private String type;


    SmsSendStatueEnum(String name, String type) {
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
