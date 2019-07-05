package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jinxin on 2017/8/9.
 * 数据提交人类型
 *    0 被调查人  1 公司人员
 */
public enum DataSubmitByTypeEnum {

    MERCHANT_STAFF("公司人员","1"),BORROWER("被调查人","0");

    private String name;

    private String type;


    DataSubmitByTypeEnum(String name, String type) {
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
