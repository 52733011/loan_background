package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.UserRoleWebapp;
import com.xiaochong.loan.background.mapper.UserRoleWebappMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("userRoleWebappDao")
public class UserRoleWebappDao {

    @Resource
    private UserRoleWebappMapper userRoleWebappMapper;

    public int insert(UserRoleWebapp userRoleWebapp) {
        return userRoleWebappMapper.insert(userRoleWebapp);
    }

    public UserRoleWebapp getByUserRoleWebapp(UserRoleWebapp userRoleWebapp) {
        return userRoleWebappMapper.getByUserRoleWebapp(userRoleWebapp);
    }

    public int insertOrUpdate(UserRoleWebapp userRoleWebapp) {
        if(userRoleWebapp==null){
            return 0;
        }
        UserRoleWebapp search = userRoleWebappMapper.selectByPrimaryKey(userRoleWebapp.getId());
        if(search==null){
            return userRoleWebappMapper.insertSelective(userRoleWebapp);
        }else{
            return userRoleWebappMapper.updateByPrimaryKeySelective(userRoleWebapp);
        }
    }

    public List<UserRoleWebapp> listByUserRoleWebapp(UserRoleWebapp userRoleWebapp) {
        return userRoleWebappMapper.listByUserRoleWebapp(userRoleWebapp);
    }

    public int delete(int id) {
        return userRoleWebappMapper.deleteByPrimaryKey(id);
    }
    public int delete(UserRoleWebapp userRoleWebapp) {
        return userRoleWebappMapper.deleteByPrimaryKey(userRoleWebapp.getId());
    }
}
