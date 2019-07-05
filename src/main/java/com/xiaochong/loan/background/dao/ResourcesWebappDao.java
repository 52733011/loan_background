package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.ResourcesWebapp;
import com.xiaochong.loan.background.entity.vo.MenuWebappVo;
import com.xiaochong.loan.background.entity.vo.ResourcesWebappPage;
import com.xiaochong.loan.background.entity.vo.ResourcesWebappVo;
import com.xiaochong.loan.background.mapper.ResourcesWebappMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("resourcesWebappDao")
public class ResourcesWebappDao {

    @Resource
    private ResourcesWebappMapper resourcesWebappMapper;

    public List<MenuWebappVo> queryResourcesUserSelect(String userId, Integer type) {
        return resourcesWebappMapper.queryResourcesUserSelect(userId,type);
    }

    public List<MenuWebappVo> queryResourcesMerchSelect(Integer merchId, Integer type) {
        return resourcesWebappMapper.queryResourcesMerchSelect(merchId,type);
    }

    public List<MenuWebappVo> queryResourcesRoleSelect(String roleId, Integer type) {
        return resourcesWebappMapper.queryResourcesRoleSelect(roleId,type);
    }

    public List<ResourcesWebapp> listByResourcesWebapp(ResourcesWebapp resources) {
        return resourcesWebappMapper.listByResourcesWebapp(resources);
    }

    public ResourcesWebapp getById(Integer id) {
        return resourcesWebappMapper.selectByPrimaryKey(id);
    }

    public int insert(ResourcesWebapp resources) {
        if(resources.getCreatetime()==null){
            resources.setCreatetime(new Date());
        }
        return resourcesWebappMapper.insertSelective(resources);
    }

    public int update(ResourcesWebapp resources) {
        if(resources.getUpdatetime()==null){
            resources.setUpdatetime(new Date());
        }
        return resourcesWebappMapper.updateByPrimaryKeySelective(resources);
    }

    public List<ResourcesWebapp> queryAllResources() {
        return resourcesWebappMapper.queryAllResources();
    }

    public List<ResourcesWebappPage> resourcesWebappPage(Integer parentId, Integer type, String resName, String resUrl) {
        return resourcesWebappMapper.resourcesWebappPage(parentId,type,resName,resUrl);
    }

    public List<ResourcesWebappVo> listResourcesWebapp(Integer merchId,Integer type) {
        return resourcesWebappMapper.listResourcesWebapp(merchId,type);
    }

    public List<MenuWebappVo> queryMerchMenu(Integer merchId, Integer roleId,Integer type) {
        return resourcesWebappMapper.queryMerchMenu(merchId,roleId,type);
    }


    public List<ResourcesWebapp> queryResourcesByUser(Integer userId, Integer type) {
        return resourcesWebappMapper.queryResourcesByUser(userId,type);
    }

    public List<ResourcesWebapp> queryResourcesByMerch(Integer merchId, Integer type) {
        return resourcesWebappMapper.queryResourcesByMerch(merchId,type);
    }
}
