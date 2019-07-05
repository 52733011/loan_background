package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.ResourceMeunManage;
import com.xiaochong.loan.background.mapper.ResourceMeunManageMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("resourceMenuManageDao")
public class ResourceMenuManageDao {

    @Resource
    private ResourceMeunManageMapper resourceMeunManageMapper;

    public List<ResourceMeunManage> listByResourceMeunManage(ResourceMeunManage resourceMeunWebapp) {
        return resourceMeunManageMapper.listByResourceMeunManage(resourceMeunWebapp);
    }

    public int delete(ResourceMeunManage resourceMeunManage) {
        return resourceMeunManageMapper.deleteByPrimaryKey(resourceMeunManage.getId());
    }

    public int insert(ResourceMeunManage resourceMeunWebapp) {
        return resourceMeunManageMapper.insertSelective(resourceMeunWebapp);
    }
}
