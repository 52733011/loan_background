package com.xiaochong.loan.background.entity.vo;

public class RateTemplateVo {

    private String id;

    private String rateName;

    private String stagingType;

    private String interestRate;

    private String serviceCharge;

    private String depositMoney;

    private String overdueRate;

    private String overdueType;

    private String createTime;

    private String createUser;
    //逾期常数
    private String overdueConstant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public String getStagingType() {
        return stagingType;
    }

    public void setStagingType(String stagingType) {
        this.stagingType = stagingType;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(String depositMoney) {
        this.depositMoney = depositMoney;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getOverdueType() {
        return overdueType;
    }

    public void setOverdueType(String overdueType) {
        this.overdueType = overdueType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getOverdueConstant() {
        return overdueConstant;
    }

    public void setOverdueConstant(String overdueConstant) {
        this.overdueConstant = overdueConstant;
    }

    @Override
    public String toString() {
        return "RateTemplateVo{" +
                "id='" + id + '\'' +
                ", rateName='" + rateName + '\'' +
                ", stagingType='" + stagingType + '\'' +
                ", interestRate='" + interestRate + '\'' +
                ", serviceCharge='" + serviceCharge + '\'' +
                ", depositMoney='" + depositMoney + '\'' +
                ", overdueRate='" + overdueRate + '\'' +
                ", overdueType='" + overdueType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createUser='" + createUser + '\'' +
                '}';
    }
}
