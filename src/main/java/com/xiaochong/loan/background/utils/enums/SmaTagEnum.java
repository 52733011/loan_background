package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/9/9.
 *
 */
public enum SmaTagEnum {
    COMPANY_NAME("公司名称","0"),
    BORROWING_INFORMATION("借款信息","1"),
    BANK_CARD_NO("用户银行卡号后4位","2"),
    NEXT_REPAYMENT("下一期待还款信息","3"),
    OVERDUE_DAYS("逾期天数","4"),
    BILLING_INFORMATION("账单信息","5"),
    REPAYMENT("还款信息","6");

    private String name;

    private String type;


    SmaTagEnum(String name, String type) {
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
