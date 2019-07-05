package com.xiaochong.loan.background.entity.vo;

public class AkkaRiskOrderParamsVo {

    private String idCard;

    private String orderNum;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "AkkaRiskOrderParamsVo{" +
                "idCard='" + idCard + '\'' +
                ", orderNum='" + orderNum + '\'' +
                '}';
    }
}
