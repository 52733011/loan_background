package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.User;
import com.xiaochong.loan.background.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

@Repository("userDao")
public class UserDao {

    @Resource
    private UserMapper userMapper;


    public User insertOrUpdateUser(User user) {
        User searchUser = new User();
        searchUser.setIdCard(user.getIdCard());
        searchUser=userMapper.getByUser(searchUser);
        int flag;
        if(searchUser!=null){
            user.setId(searchUser.getId());
            flag = this.update(user);
        }else{
            flag = this.insert(user);
        }
        return flag!=0?user:null;
    }

    private int insert(User user) {
        return userMapper.insertSelective(user);
    }


    public int update(User user) {
        if(user.getUpdatetime()==null){
            user.setUpdatetime(new Date());
        }
        return userMapper.updateByPrimaryKeySelective(user);
    }


    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
