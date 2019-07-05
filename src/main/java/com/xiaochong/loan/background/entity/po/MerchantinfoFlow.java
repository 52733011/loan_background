package com.xiaochong.loan.background.entity.po;

public class MerchantinfoFlow {
	
	/** 商户表id */
	private Integer id;
	/** 认证流程（loan_checkflow表的id） */
	private Integer merchId;
	private String flowNo;
	private Integer flowStep;

    public Integer getFlowStep() {
        return flowStep;
    }

    public void setFlowStep(Integer flowStep) {
        this.flowStep = flowStep;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    @Override
    public String toString() {
        return "MerchantinfoFlow{" +
                "id=" + id +
                ", merchId=" + merchId +
                ", flowNo='" + flowNo + '\'' +
                ", flowStep=" + flowStep +
                '}';
    }
}
