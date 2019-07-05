package com.xiaochong.loan.background.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel
public class ResourcesWebappPage {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "权限名")
    private String resName;
    @ApiModelProperty(value = "资源url")
    private String resUrl;
    @ApiModelProperty(value = "权限说明")
    private String resRemark;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "权限类型   1:菜单    2：接口",example = "1")
    private Integer type;
    @ApiModelProperty(value = "父资源,若无父级则为0")
    private Integer parentId;
    @ApiModelProperty(value = "父权限名称")
    private String parentName;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "更新时间")
    private String updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public String getResRemark() {
        return resRemark;
    }

    public void setResRemark(String resRemark) {
        this.resRemark = resRemark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
