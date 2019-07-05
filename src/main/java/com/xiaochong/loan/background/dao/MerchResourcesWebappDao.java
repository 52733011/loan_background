package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.MerchResourcesWebapp;
import com.xiaochong.loan.background.mapper.MerchResourcesWebappMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("merchResourcesWebappDao")
public class MerchResourcesWebappDao {

    @Resource
    private MerchResourcesWebappMapper merchResourcesWebappMapper;


    public int insert(MerchResourcesWebapp merchResourcesWebapp) {
        return merchResourcesWebappMapper.insertSelective(merchResourcesWebapp);
    }

    public List<MerchResourcesWebapp> listByMerchResourcesWebapp(MerchResourcesWebapp merchResourcesWebapp) {
        return merchResourcesWebappMapper.listByMerchResourcesWebapp(merchResourcesWebapp);
    }

    public int delete(Integer id) {
        return merchResourcesWebappMapper.deleteByPrimaryKey(id);
    }
}
