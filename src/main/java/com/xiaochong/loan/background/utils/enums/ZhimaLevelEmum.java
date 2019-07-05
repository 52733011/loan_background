package com.xiaochong.loan.background.utils.enums;

public enum  ZhimaLevelEmum {
    LOW("1","低"),MIDDLE("2","中"),HIGH("3","高");

    private String type;

    private String name;

    ZhimaLevelEmum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByType(String type){
        switch (type){
            case "1":
                return ZhimaLevelEmum.LOW.getName();
            case "2":
                return ZhimaLevelEmum.MIDDLE.getName();
            case "3":
                return ZhimaLevelEmum.HIGH.getName();
            default:
                return "-";
        }
    }
}
