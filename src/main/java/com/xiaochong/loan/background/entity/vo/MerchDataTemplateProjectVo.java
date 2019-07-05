package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MerchDataTemplateProjectVo {
    @ApiModelProperty(value = "主键")
    private Integer id;
    @ApiModelProperty(value = "数据模板id")
    private Integer dataTempId;
    @ApiModelProperty(value = "projectId")
    private Integer projectId;
    @ApiModelProperty(value = "数据模板项目名")
    private String projectName;
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    @ApiModelProperty(value = "文件名称")
    private String filename;
    @ApiModelProperty(value = "项目备注")
    private String mark;
    @ApiModelProperty(value = "项目数据 当选择为暂存时会将数据模板项目中已有的数据填入")
    private String data;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataTempId() {
        return dataTempId;
    }

    public void setDataTempId(Integer dataTempId) {
        this.dataTempId = dataTempId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}