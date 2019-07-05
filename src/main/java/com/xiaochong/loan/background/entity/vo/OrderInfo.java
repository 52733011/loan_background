package com.xiaochong.loan.background.entity.vo;

public class OrderInfo {

    private String orderNo;

    private String createdAt;

    private String gisLatitude;

    private String gisLongitude;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    public String getGisLatitude() {
        return gisLatitude;
    }

    public void setGisLatitude(String gisLatitude) {
        this.gisLatitude = gisLatitude;
    }

    public String getGisLongitude() {
        return gisLongitude;
    }

    public void setGisLongitude(String gisLongitude) {
        this.gisLongitude = gisLongitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "OrderInfoVo{" +
                "orderNo='" + orderNo + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", gisLatitude='" + gisLatitude + '\'' +
                ", gisLongitude='" + gisLongitude + '\'' +
                '}';
    }
}
