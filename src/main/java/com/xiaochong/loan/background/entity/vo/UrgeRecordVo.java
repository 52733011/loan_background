package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel
public class UrgeRecordVo {
@ApiModelProperty("催记id")
    private Integer id;
    @ApiModelProperty("催收逾期id")
    private Integer urgeOverDueId;
    @ApiModelProperty("序号")
    private Integer stageNo;
    @ApiModelProperty("催记内容")
    private String urgeContent;
    @ApiModelProperty("催记人员id")
    private Integer createUser;
    @ApiModelProperty("催记人员电话")
    private String createUserPhone;
    @ApiModelProperty("催记人员姓名")
    private String createUserName;
    @ApiModelProperty("催记时间")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUrgeOverDueId() {
        return urgeOverDueId;
    }

    public void setUrgeOverDueId(Integer urgeOverDueId) {
        this.urgeOverDueId = urgeOverDueId;
    }

    public Integer getStageNo() {
        return stageNo;
    }

    public void setStageNo(Integer stageNo) {
        this.stageNo = stageNo;
    }

    public String getUrgeContent() {
        return urgeContent;
    }

    public void setUrgeContent(String urgeContent) {
        this.urgeContent = urgeContent;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserPhone() {
        return createUserPhone;
    }

    public void setCreateUserPhone(String createUserPhone) {
        this.createUserPhone = createUserPhone;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}