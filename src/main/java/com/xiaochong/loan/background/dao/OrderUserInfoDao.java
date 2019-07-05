package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.OrderUserInfo;
import com.xiaochong.loan.background.mapper.OrderUserInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

@Repository("orderUserInfoDao")
public class OrderUserInfoDao {

    @Resource
    private OrderUserInfoMapper orderUserInfoMapper;

    public OrderUserInfo getByOrderUserInfo(OrderUserInfo orderUserInfo) {
        return orderUserInfoMapper.getByOrderUserInfo(orderUserInfo);
    }

    public int insert(OrderUserInfo orderUserInfo) {
        if(orderUserInfo.getCreatetime()==null){
            orderUserInfo.setCreatetime(new Date());
        }
        return orderUserInfoMapper.insertSelective(orderUserInfo);
    }

    public int update(OrderUserInfo orderUserInfo) {
        if(orderUserInfo.getUpdatetime()==null){
            orderUserInfo.setUpdatetime(new Date());
        }
        return orderUserInfoMapper.updateByPrimaryKeySelective(orderUserInfo);
    }

    public OrderUserInfo selectById(Integer userId) {
        return orderUserInfoMapper.selectByPrimaryKey(userId);
    }
}
