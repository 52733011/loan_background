package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/5/24.
 */
public enum ContactInfoRelationEnum {
    TYPE_FATHER("父亲","1"),TYPE_MOTHER("母亲","2"),TYPE_COUPLE("配偶","3"),
    TYPE_ROMMER("室友","6"),TYPE_TEACHER("老师","7"), TYPE_FRIEND("朋友","9"),
    TYPE_LOVER("恋人","10"),TYPE_RELATIVE("亲戚","11"),NO_TYPE("其他","12");

    private String name;

    private String type;


    ContactInfoRelationEnum(String name, String type) {
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

    public static ContactInfoRelationEnum getContactInfoRelationByType(String type){
        for (ContactInfoRelationEnum value:ContactInfoRelationEnum.values() ) {
            if(value.getType().equals(type)){return value;}
        }
        return null;
    }
}
