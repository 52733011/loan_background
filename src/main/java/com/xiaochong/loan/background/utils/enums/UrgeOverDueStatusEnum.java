package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 催收逾期状态
 *  1 今日已跟进 2 待跟进  3  已完成
 */
public enum UrgeOverDueStatusEnum {

    URGEED_TODAY("今日已跟进","1"),WAITING_URGE("待跟进","2"),OFF_THE_STOCKS("已完成","3");

    private String name;

    private String type;


    UrgeOverDueStatusEnum(String name, String type) {
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
