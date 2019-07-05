
package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ReportResultVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "报告号")
    private String reportNo;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "报告内容")
    private String reportContent;
    @ApiModelProperty(value = "商户名称")
    private String merName;
    @ApiModelProperty(value = "订单时间")
    private String orderTime;

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    @Override
    public String toString() {
        return "ReportResultVo{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", reportNo='" + reportNo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", reportContent='" + reportContent + '\'' +
                ", merName='" + merName + '\'' +
                ", orderTime='" + orderTime + '\'' +
                '}';
    }
}