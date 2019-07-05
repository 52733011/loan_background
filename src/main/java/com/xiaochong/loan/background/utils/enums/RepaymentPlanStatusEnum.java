package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/9/11.
 * 还款状态
 */
public enum RepaymentPlanStatusEnum {
    NOT_PAY("待还","1"),PAY("已还","2"),OVERDUE("逾期待还","3");

    private String name;

    private String type;


    RepaymentPlanStatusEnum(String name, String type) {
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

    public static RepaymentPlanStatusEnum getContactInfoRelationByType(String type){
        for (RepaymentPlanStatusEnum value: RepaymentPlanStatusEnum.values() ) {
            if(value.getType().equals(type)){return value;}
        }
        return null;
    }
}
