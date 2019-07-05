package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
@ApiModel
public class RepaymentPlanVo {
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("放款申请id")
    private Integer applicationId;
    @ApiModelProperty("序号 （当前期数）")
    private Integer stageNo;
    @ApiModelProperty("还款时间(应还日期)")
    private String returnTime;
    @ApiModelProperty("应还本金(应还本金)")
    private BigDecimal principalMoney;
    @ApiModelProperty("应还利息(应还利息服务费)")
    private BigDecimal interest;
    @ApiModelProperty("罚息(逾期管理费)")
    private BigDecimal punishInterest;
    @ApiModelProperty("全部待还(应还金额)")
    private BigDecimal wholeMoney;
    @ApiModelProperty("逾期天数")
    private Integer overdueDays;
    @ApiModelProperty("状态 1 待还  2 已还  3 逾期待还")
    private String status;
    @ApiModelProperty("最终还款时间")
    private String practicalTime;
    @ApiModelProperty("创建人")
    private Integer createBy;
    @ApiModelProperty("创建人")
    private String createByName;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("更新人")
    private Integer updateBy;
    @ApiModelProperty("更新人")
    private String updateByName;
    @ApiModelProperty("更新时间")
    private String updateTime;

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getStageNo() {
        return stageNo;
    }

    public void setStageNo(Integer stageNo) {
        this.stageNo = stageNo;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public BigDecimal getPrincipalMoney() {
        return principalMoney;
    }

    public void setPrincipalMoney(BigDecimal principalMoney) {
        this.principalMoney = principalMoney;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getPunishInterest() {
        return punishInterest;
    }

    public void setPunishInterest(BigDecimal punishInterest) {
        this.punishInterest = punishInterest;
    }

    public BigDecimal getWholeMoney() {
        return wholeMoney;
    }

    public void setWholeMoney(BigDecimal wholeMoney) {
        this.wholeMoney = wholeMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPracticalTime() {
        return practicalTime;
    }

    public void setPracticalTime(String practicalTime) {
        this.practicalTime = practicalTime;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}