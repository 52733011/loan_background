package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/8/14.
 */
public enum CheckflowTypeEnum {
    OPERATOR("运营商认证","002"),ACADEMIC("学信网认证","005"),ZHIMA("芝麻认证","007")
    ,USERINFO("个人基础信息","010"),CONTACT("添加联系人","011"),OPERATOR_REPORT("运营商报告","0021")
    ,ACADEMIC_NEW("学信网认证(新)","012") ,BANK_CARD("绑卡","014"),ACADEMIC_TONGDUN("学信网认证(同盾)","0015") ;

    private String name;

    private String type;


    CheckflowTypeEnum(String name, String type) {
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
