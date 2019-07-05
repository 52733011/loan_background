package com.xiaochong.loan.background.utils.enums;

public enum TongdunRuleTypeEnum {
    loan("借贷记录",0),black("不良信息",1);

    private String name;

    private Integer type;

    TongdunRuleTypeEnum(String name, Integer type) {
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
