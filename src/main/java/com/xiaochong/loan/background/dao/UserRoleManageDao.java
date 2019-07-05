package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.UserRoleManage;
import com.xiaochong.loan.background.mapper.UserRoleManageMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("userRoleManageDao")
public class UserRoleManageDao {

    @Resource
    private UserRoleManageMapper userRoleManageMapper;

    public int insert(UserRoleManage userRoleManage) {
        return userRoleManageMapper.insertSelective(userRoleManage);
    }

    public UserRoleManage getByUserRoleManage(UserRoleManage userRoleManage) {
        return userRoleManageMapper.getByUserRoleManage(userRoleManage);
    }

    public int insertOrUpdate(UserRoleManage userRoleManage) {
        if(userRoleManage.getId()!=null){
            return this.update(userRoleManage);
        }else{
            return this.insert(userRoleManage);
        }
    }

    private int update(UserRoleManage userRoleManage) {
        return userRoleManageMapper.updateByPrimaryKeySelective(userRoleManage);
    }
}
