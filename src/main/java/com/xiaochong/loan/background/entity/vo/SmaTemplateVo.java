package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;


@ApiModel
public class SmaTemplateVo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "公司名")
    private String merchantName;
    @ApiModelProperty(value = "触发状态 0 分期放款成功")
    private String sendStatus;
    @ApiModelProperty(value = "状态 0 禁用 1 启用")
    private String status;
    @ApiModelProperty(value = "修改人")
    private String updateUser;
    @ApiModelProperty(value = "创建时间")
    private String createtime;
    @ApiModelProperty(value = "修改时间")
    private String updatetime;
    @ApiModelProperty(value = "模板内容")
    private String content;
    @ApiModelProperty(value = "模板示例")
    private String sample;
    @ApiModelProperty(value = "当月发送次数")
    private Long monthCount;
    @ApiModelProperty(value = "所有次数次数")
    private Long allCount;

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

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public Long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Long monthCount) {
        this.monthCount = monthCount;
    }

    public Long getAllCount() {
        return allCount;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
