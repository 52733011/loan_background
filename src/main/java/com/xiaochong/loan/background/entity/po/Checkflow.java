package com.xiaochong.loan.background.entity.po;

public class Checkflow {

	
	private String id;
	/** 认证流程名称 */
	private String flowName;

	private String flowNo;

	private String status;

	private String url;
	
	private String type;

	private Integer step;

    public Integer getStep() {
        return step;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    @Override
    public String toString() {
        return "LoanCheckflow{" +
                "id='" + id + '\'' +
                ", flowname='" + flowName + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
