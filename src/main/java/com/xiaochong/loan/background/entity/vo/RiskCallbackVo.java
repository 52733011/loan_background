package com.xiaochong.loan.background.entity.vo;

public class RiskCallbackVo {

    private String callback_url;

    private String certNo;

    private String orderNum;

    private String redirect_url;

    private String yys_report_url;

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getYys_report_url() {
        return yys_report_url;
    }

    public void setYys_report_url(String yys_report_url) {
        this.yys_report_url = yys_report_url;
    }

    @Override
    public String toString() {
        return "RiskCallbackVo{" +
                "callback_url='" + callback_url + '\'' +
                ", certNo='" + certNo + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", redirect_url='" + redirect_url + '\'' +
                ", yys_report_url='" + yys_report_url + '\'' +
                '}';
    }
}
