package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/9/11.
 * 逾期记发
 */
public enum RateTemplateOverdueMoneyTypeEnum {
    PRINCIPAL("本金",0), PRINCIPAL_INTEREST("本息",1);

    private String name;

    private Integer type;


    RateTemplateOverdueMoneyTypeEnum(String name, Integer type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static RateTemplateOverdueMoneyTypeEnum getContactInfoRelationByType(Integer type){
        for (RateTemplateOverdueMoneyTypeEnum value: RateTemplateOverdueMoneyTypeEnum.values() ) {
            if(value.getType().equals(type)){return value;}
        }
        return null;
    }
}
