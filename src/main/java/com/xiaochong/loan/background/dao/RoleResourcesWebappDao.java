package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.RoleResourcesWebapp;
import com.xiaochong.loan.background.mapper.RoleResourcesWebappMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("roleResourcesWebappDao")
public class RoleResourcesWebappDao {

    @Resource
    private RoleResourcesWebappMapper roleResourcesWebappMapper;

    public int insert(RoleResourcesWebapp roleResourcesWebapp) {
        return roleResourcesWebappMapper.insertSelective(roleResourcesWebapp);
    }

    public List<RoleResourcesWebapp> listByRoleResourcesWebapp(RoleResourcesWebapp roleResourcesWebapp) {
        return roleResourcesWebappMapper.listByRoleResourcesWebapp(roleResourcesWebapp);
    }

    public int delete(Integer id) {
        return roleResourcesWebappMapper.deleteByPrimaryKey(id);
    }

    public RoleResourcesWebapp getByRoleResourcesWebapp(RoleResourcesWebapp roleResourcesWebapp) {
        return roleResourcesWebappMapper.getByRoleResourcesWebapp(roleResourcesWebapp);
    }

    public int delete(RoleResourcesWebapp roleResourcesWebapp) {
        return roleResourcesWebappMapper.deleteByPrimaryKey(roleResourcesWebapp.getId());
    }
}
