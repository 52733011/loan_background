package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.ResourcesManage;
import com.xiaochong.loan.background.entity.vo.ResourcesManagePage;
import com.xiaochong.loan.background.mapper.ResourcesManageMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("resourcesManageDao")
public class ResourcesManageDao {

    @Resource
    private ResourcesManageMapper resourcesManageMapper;

    public int insert(ResourcesManage resources) {
        if(resources.getCreatetime()==null){
            resources.setCreatetime(new Date());
        }
        return resourcesManageMapper.insertSelective(resources);
    }

    public int update(ResourcesManage resources) {
        if(resources.getUpdatetime()==null){
            resources.setUpdatetime(new Date());
        }
        return resourcesManageMapper.updateByPrimaryKeySelective(resources);
    }

    public List<ResourcesManage> queryAllResources(Integer type) {
        return resourcesManageMapper.queryAllResources(type);
    }

    public List<ResourcesManagePage> resourcesManagePage(String resName, String resUrl, Integer parentId, Integer type) {
        return resourcesManageMapper.resourcesManagePage(resName,resUrl,parentId, type);
    }

    public List<ResourcesManage> queryResources(Integer userId) {
        return this.queryResources(userId,null);
    }

    public List<ResourcesManage> queryResources(Integer userId, Integer type) {
        return resourcesManageMapper.queryResources(userId,type);
    }

    public ResourcesManage getById(Integer id) {
        return resourcesManageMapper.selectByPrimaryKey(id);
    }


}
