package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/9/11.
 * 逾期记发
 */
public enum RateTemplateOverdueTypeEnum {
    CURRENT("当期待还",0), ALL("全部待还",1);

    private String name;

    private Integer type;


    RateTemplateOverdueTypeEnum(String name, Integer type) {
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

    public static RateTemplateOverdueTypeEnum getContactInfoRelationByType(Integer type){
        for (RateTemplateOverdueTypeEnum value: RateTemplateOverdueTypeEnum.values() ) {
            if(value.getType().equals(type)){return value;}
        }
        return null;
    }
}
