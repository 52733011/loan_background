package com.xiaochong.loan.background.service;

import com.xiaochong.loan.background.dao.AdminDao;
import com.xiaochong.loan.background.entity.po.Admin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {

    @Resource
    private AdminDao adminDao;

    public Admin adminTest(){
        return adminDao.select();
    }

}
