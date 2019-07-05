package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.RoleManage;
import com.xiaochong.loan.background.entity.vo.RoleManageVo;
import com.xiaochong.loan.background.mapper.RoleManageMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("roleManageDao")
public class RoleManageDao {

    @Resource
    private RoleManageMapper roleManageMapper;

    public int insert(RoleManage roleManage) {
        if(roleManage.getCreatetime()==null){
            roleManage.setCreatetime(new Date());
        }
        return roleManageMapper.insertSelective(roleManage);
    }

    public List<RoleManageVo> roleManagePage() {
        return roleManageMapper.roleManagePage();
    }

    public RoleManage getById(Integer id) {
        return roleManageMapper.selectByPrimaryKey(id);
    }

    public int update(RoleManage roleManage) {
        if(roleManage.getUpdatetime()==null){
            roleManage.setUpdatetime(new Date());
        }
        return roleManageMapper.updateByPrimaryKeySelective(roleManage);
    }

    public RoleManage getByUserId(Integer userId) {
        return roleManageMapper.getByUserId(userId);
    }
}
