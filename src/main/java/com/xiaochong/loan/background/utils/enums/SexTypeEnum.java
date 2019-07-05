package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/5/24.
 * 自增编号类型
 */
public enum SexTypeEnum {

    WOMAN("女","0"),MAN("男","1");

    private String name;

    private String type;


    SexTypeEnum(String name, String type) {
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
                return WOMAN.getName();
            case "1":
                return MAN.getName();
            default:
                return null;
        }
    }
}
