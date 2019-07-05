package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/10/26.
 * 百度坐标的类型
 */
public enum BaiduCoordTypeEnum {

    BAIDU("百度经纬度坐标","bd09ll"),GPS("GPS经纬度","wgs84ll");

    private String name;

    private String type;


    BaiduCoordTypeEnum(String name, String type) {
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

    public static String getName(String type){
        switch (type){
            case "0":
                return BAIDU.getName();
            case "1":
                return GPS.getName();
            default:
                return null;
        }
    }
}
