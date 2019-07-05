package com.xiaochong.loan.background.utils.enums;

/**
 * Created by ray.liu on 2017/5/10.
 */
public enum MerchFlowNoEnum {

    ZHIMA("201708111938421190000","芝麻"),EDUCATION("201708111938421200002","学信")
    ,OPERATOR("201708111938421200001","运营商"),BASE_INFO("201708111938421200004","个人基础信息")
    ,EDUCATION_NEW("201708111938421200006","学信(新)"),OPERATOR_REPORT("201708111938421200007","运营商报告")
    ,BANK_CARD("201708111938421200008","绑卡")
    ,LINK_MAN("201708111938421220005","联系人"),EDUCATION_TONGDUN("201708111938421200009","学历(同盾)");

    MerchFlowNoEnum(String flowNo, String name) {
        this.flowNo = flowNo;
        this.name = name;
    }

    private String flowNo;

    private String name;

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
