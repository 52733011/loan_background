package com.xiaochong.loan.background.utils.enums;

/**
 * Created by jixnin on 2017/9/5.
 * 放贷申请状态
 *     1 待提交  2 待审核 3  已放款  4重新提交 5 已拒绝
 */
public enum LoanApplicationStatusEnum {

    WAITING_SUBMIT("待提交","1"),WAITING_AUDIT("待审核","2"),LOANED("已放款","3")
    ,RESUBMIT("重新提交","4"),REFUSED("已拒绝","5");

    private String name;

    private String type;


    LoanApplicationStatusEnum(String name, String type) {
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
