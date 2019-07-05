package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.RoleResourcesManage;
import com.xiaochong.loan.background.entity.vo.RoleResourcesManageVo;
import com.xiaochong.loan.background.mapper.RoleResourcesManageMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("roleResourcesManageDao")
public class RoleResourcesManageDao {

    @Resource
    private RoleResourcesManageMapper roleResourcesManageMapper;

    public List<RoleResourcesManage> listByRoleResourcesManage(RoleResourcesManage roleResourcesManage) {
        return roleResourcesManageMapper.listByRoleResourcesManage(roleResourcesManage);
    }

    public List<RoleResourcesManageVo> listRoleResourcesManageVo(Integer roleId,Integer type) {
        return roleResourcesManageMapper.listRoleResourcesManageVo(roleId,type);
    }

    public int delete(Integer id) {
        return roleResourcesManageMapper.deleteByPrimaryKey(id);
    }

    public int insert(RoleResourcesManage roleResourcesManage) {
        return roleResourcesManageMapper.insertSelective(roleResourcesManage);
    }

    public int delete(RoleResourcesManage roleResourcesManage) {
        return roleResourcesManageMapper.deleteByPrimaryKey(roleResourcesManage.getId());
    }
}
