package com.xiaochong.loan.background.entity.vo;

import com.xiaochong.loan.background.entity.po.ResourcesWebapp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ResourcesWebappVo {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "商户id")
    private Integer merchId;
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
    @ApiModelProperty(value = "是否选中")
    private boolean select;
    @ApiModelProperty(value = "父资源,若无父级则为0")
    private Integer parentId;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "子菜单")
    private List<ResourcesWebappVo> sonResources;

    public void setResourcesWebapp(ResourcesWebapp resourcesWebapp) {
        this.id = resourcesWebapp.getId();
        this.resName = resourcesWebapp.getResName();
        this.resUrl = resourcesWebapp.getResUrl();
        this.resRemark = resourcesWebapp.getResRemark();
        this.status = resourcesWebapp.getStatus();
        this.type = resourcesWebapp.getType();
        this.parentId = resourcesWebapp.getParentId();
        this.sort = resourcesWebapp.getSort();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<ResourcesWebappVo> getSonResources() {
        return sonResources;
    }

    public void setSonResources(List<ResourcesWebappVo> sonResources) {
        this.sonResources = sonResources;
    }
}
