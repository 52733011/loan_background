package com.xiaochong.loan.background.utils.enums;

public enum TableTypeEnum {
    normal("正常表格",0),remark("带备注表格",1),callRecord("通话记录",2);

    private String name;

    private Integer type;

    TableTypeEnum(String name, Integer type) {
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
