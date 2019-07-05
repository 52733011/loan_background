package com.xiaochong.loan.background.entity.vo;


import com.xiaochong.loan.background.utils.enums.SmaTagEnum;

public class LoanSmsTemplate {
    private SmaTagEnum smaTagEnum;
    private Object param;

    public SmaTagEnum getSmaTagEnum() {
        return smaTagEnum;
    }

    public void setSmaTagEnum(SmaTagEnum smaTagEnum) {
        this.smaTagEnum = smaTagEnum;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
