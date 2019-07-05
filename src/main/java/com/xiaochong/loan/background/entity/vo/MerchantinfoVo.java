package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
public class MerchantinfoVo {

    @ApiModelProperty(value = "商户主键")
	private Integer id;
	/** 商户名称 */
    @ApiModelProperty(value = "商户名称")
	private String merchantName;
	/** 商户简称 */
    @ApiModelProperty(value = "商户简称")
    private String shortName;
    /** 英文简称*/
    @ApiModelProperty(value = "英文简称")
    private String enName;
    /** 服务开始时间 */
    @ApiModelProperty(value = "服务开始时间")
    private String cooperantStartTime;
    /** 服务结束时间 */
    @ApiModelProperty(value = "服务结束时间")
    private String cooperantStopTime;
    /** 状态 */
    @ApiModelProperty(value = "状态 0-启用 1-停用")
    private String status;
    /** 联系人 */
    @ApiModelProperty(value = "联系人")
    private String linkMan;
    /** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    private String linkMobile;
    /** 邮箱 */
    @ApiModelProperty(value = "邮箱")
    private String linkEmail;
    /** 公司地址 */
    @ApiModelProperty(value = "公司地址")
    private String address;
    /** 公司规模 */
    @ApiModelProperty(value = "公司规模")
    private String scale;
    /** 行业 */
    @ApiModelProperty(value = "行业")
    private String industry;
    /** 营业执照连接 */
    @ApiModelProperty(value = "营业执照连接")
    private String certificateUrl;
    @ApiModelProperty(value = "营业执照名称")
    private String certificateName;
    /** 服务协议链接 */
    @ApiModelProperty(value = "服务协议链接")
    private String agreementUrl;
    /** 服务协议名称 */
    @ApiModelProperty(value = "服务协议名称")
    private String agreementName;
    /** 审核单位 */
    @ApiModelProperty(value = "审核单位")
    private String auditType;

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getCooperantStartTime() {
        return cooperantStartTime;
    }

    public void setCooperantStartTime(String cooperantStartTime) {
        this.cooperantStartTime = cooperantStartTime;
    }

    public String getAgreementName() {
        return agreementName;
    }

    public void setAgreementName(String agreementName) {
        this.agreementName = agreementName;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    /** 营业执照名称 */

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCooperantStopTime() {
        return cooperantStopTime;
    }

    public void setCooperantStopTime(String cooperantStopTime) {
        this.cooperantStopTime = cooperantStopTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }


    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
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

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    @Override
    public String toString() {
        return "MerchantinfoVo{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", enName='" + enName + '\'' +
                ", cooperantStopTime='" + cooperantStopTime + '\'' +
                ", status='" + status + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", linMobile='" + linkMobile + '\'' +
                ", linkEmail='" + linkEmail + '\'' +
                ", address='" + address + '\'' +
                ", scale='" + scale + '\'' +
                ", industry='" + industry + '\'' +
                ", certificateUrl='" + certificateUrl + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", agreementUrl='" + agreementUrl + '\'' +
                ", agreementName='" + agreementName + '\'' +
                '}';
    }
}
