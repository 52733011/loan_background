package com.xiaochong.loan.background.entity.po;

import java.util.Date;

public class Merchantinfo {

	private Integer id;
	/** 商户名称 */
	private String merchantName;
	/** 商户简称 */
	private String shortName;
	/** 英文简称*/
	private String enName;
	/** 联系人 */
	private String linkMan;
    /** 联系电话 */
    private String linkMobile;
    /** 邮箱 */
    private String linkEmail;
	/** 公司地址 */
	private String address;
	/** 公司规模 */
	private String scale;
	/** 行业 */
	private String industry;
	/** 营业执照名称 */
	private String certificateName;
	/** 营业执照连接 */
	private String certificateUrl;
	/** 服务协议名称 */
	private String agreementName;
	/** 服务协议链接 */
	private String agreementUrl;
	/** 服务开始时间 */
	private Date cooperantBeginTime;
	/** 服务结束时间 */
	private Date cooperantStopTime;
	/** 状态 */
	private String status;
    private String auditType;
	/** 创建时间 */
	private Integer createBy;
	/** 创建时间 */
	private Integer updateBy;
	/** 创建时间 */
	private Date createtime;
	/** 修改时间 */
	private Date updatetime;

	/** 查询 开始时间 */
	private Date startTime;

	/** 查询 结束 时间 */
	private Date endTime;

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }

    public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getCertificateUrl() {
		return certificateUrl;
	}

	public void setCertificateUrl(String certificateUrl) {
		this.certificateUrl = certificateUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getAgreementName() {
		return agreementName;
	}

	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}

	public String getAgreementUrl() {
		return agreementUrl;
	}

	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}

    public Date getCooperantBeginTime() {
        return cooperantBeginTime;
    }

    public void setCooperantBeginTime(Date cooperantBeginTime) {
        this.cooperantBeginTime = cooperantBeginTime;
    }

    public Date getCooperantStopTime() {
        return cooperantStopTime;
    }

    public void setCooperantStopTime(Date cooperantStopTime) {
        this.cooperantStopTime = cooperantStopTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    @Override
    public String toString() {
        return "Merchantinfo{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", enName='" + enName + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", linkMobile='" + linkMobile + '\'' +
                ", linkEmail='" + linkEmail + '\'' +
                ", address='" + address + '\'' +
                ", scale='" + scale + '\'' +
                ", industry='" + industry + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", certificateUrl='" + certificateUrl + '\'' +
                ", agreementName='" + agreementName + '\'' +
                ", agreementUrl='" + agreementUrl + '\'' +
                ", cooperantBeginTime=" + cooperantBeginTime +
                ", cooperantStopTime=" + cooperantStopTime +
                ", status='" + status + '\'' +
                ", auditType='" + auditType + '\'' +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
