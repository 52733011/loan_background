package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/5/24.
 */
public enum ResourcesWebappTypeEnum {
    MENU("菜单",1),INTERFACE("接口",2),TOOL("工具",3),BUTTON("按钮",4);

    private String name;

    private Integer type;


    ResourcesWebappTypeEnum(String name, Integer type) {
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

    public static ResourcesWebappTypeEnum getContactInfoRelationByType(String type){
        for (ResourcesWebappTypeEnum value: ResourcesWebappTypeEnum.values() ) {
            if(value.getType().equals(type)){return value;}
        }
        return null;
    }
}
