package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.RoleWebapp;
import com.xiaochong.loan.background.entity.vo.RoleWebappVo;
import com.xiaochong.loan.background.mapper.RoleWebappMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("roleWebappDao")
public class RoleWebappDao {

    @Resource
    private RoleWebappMapper roleWebappMapper;

    public List<RoleWebappVo> roleWebappPage(String proxyUserId) {
        return roleWebappMapper.roleWebappPage(proxyUserId);
    }

    public int insert(RoleWebapp roleWebapp) {
        if(roleWebapp.getCreatetime()==null){
            roleWebapp.setCreatetime(new Date());
        }
        return roleWebappMapper.insertSelective(roleWebapp);
    }

    public RoleWebapp getById(Integer id) {
        return roleWebappMapper.selectByPrimaryKey(id);
    }

    public int update(RoleWebapp roleWebapp) {
        if(roleWebapp.getUpdatetime()==null){
            roleWebapp.setUpdatetime(new Date());
        }
        return roleWebappMapper.updateByPrimaryKeySelective(roleWebapp);
    }

    public RoleWebapp getByUserId(Integer userId) {
        return roleWebappMapper.getByUserId(userId);
    }
}
