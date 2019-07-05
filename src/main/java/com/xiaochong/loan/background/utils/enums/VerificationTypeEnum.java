package com.xiaochong.loan.background.utils.enums;


/**
 * Created by wujiaxing on 2017/5/24.
 * 自增编号类型
 */
public enum VerificationTypeEnum {

    REGISTER("注册",0),LOGIN("登录",1),FORGET_PASSWORD("忘记密码",4);

    private String name;

    private Integer type;


    VerificationTypeEnum(String name, Integer type) {
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
}
