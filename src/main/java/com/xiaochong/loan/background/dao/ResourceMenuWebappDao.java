package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.ResourceMeunWebapp;
import com.xiaochong.loan.background.mapper.ResourceMeunWebappMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("resourceMenuWebappDao")
public class ResourceMenuWebappDao {

    @Resource
    private ResourceMeunWebappMapper resourceMeunWebappMapper;

    public int insert(ResourceMeunWebapp resourceMeunWebapp) {
        return resourceMeunWebappMapper.insertSelective(resourceMeunWebapp);
    }

    public int delete(ResourceMeunWebapp resourceMeunWebapp) {
        return resourceMeunWebappMapper.deleteByPrimaryKey(resourceMeunWebapp.getId());
    }

    public int delete(Integer id) {
        return resourceMeunWebappMapper.deleteByPrimaryKey(id);
    }

    public List<ResourceMeunWebapp> listByResourceMeunWebapp(ResourceMeunWebapp resourceMeunWebapp) {
        return resourceMeunWebappMapper.listByResourceMeunWebapp(resourceMeunWebapp);
    }
}
