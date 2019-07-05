package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.Admin;
import com.xiaochong.loan.background.mapper.AdminMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AdminDao {

    @Resource
    private AdminMapper adminMapper;

    public Admin select(){
        return adminMapper.selectByPrimaryKey(1);
    }
}
