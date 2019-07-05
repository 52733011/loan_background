package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.ResourcesManage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ResourcesManageVo {
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
    private Integer parentid;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "子菜单")
    private List<ResourcesManageVo> sonResources;

    public void setResourcesWebapp(ResourcesManage resourcesManage) {
        this.id = resourcesManage.getId();
        this.resName = resourcesManage.getResName();
        this.resUrl = resourcesManage.getResUrl();
        this.resRemark = resourcesManage.getResRemark();
        this.status = resourcesManage.getStatus();
        this.type = resourcesManage.getType();
        this.parentid = resourcesManage.getParentId();
        this.sort = resourcesManage.getSort();
    }

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

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<ResourcesManageVo> getSonResources() {
        return sonResources;
    }

    public void setSonResources(List<ResourcesManageVo> sonResources) {
        this.sonResources = sonResources;
    }
}
