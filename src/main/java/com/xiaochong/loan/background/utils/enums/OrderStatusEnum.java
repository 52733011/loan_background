package com.xiaochong.loan.background.utils.enums;

/**
 * Created by wujiaxing on 2017/5/24.
 */
public enum OrderStatusEnum {
    //0未授权 1认证中 2报告生成中 3已拒绝 4已取消 5报告完成 6已过期 7异常
    UNAUTH("未授权","0"),AUTH("认证中","1"),REPORTING("报告生成中","2")
    ,REJECT("已拒绝","3"),CANCEL("已取消","4"),
    FINISH("报告完成","5"),PASTDUE("已过期","6"),UNUSUAL("异常","7");

    private String name;

    private String type;


    OrderStatusEnum(String name, String type) {
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
