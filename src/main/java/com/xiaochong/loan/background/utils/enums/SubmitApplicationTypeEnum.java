package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/5/24.
 * 数据提交方式
 */
public enum SubmitApplicationTypeEnum {

    WEB("网页","1"),APP("手机","2");

    private String name;

    private String type;


    SubmitApplicationTypeEnum(String name, String type) {
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
