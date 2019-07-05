package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/5/24.
 * 自增编号类型
 */
public enum SubmitDataTypeEnum {

    SUBMIT_APPLICATION("提交申请","1"),TEMPORARY_STORAGE ("暂存","0");

    private String name;

    private String type;


    SubmitDataTypeEnum(String name, String type) {
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
