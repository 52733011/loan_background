package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchantRecharge;
import com.xiaochong.loan.background.mapper.MerchantMapper;
import com.xiaochong.loan.background.mapper.MerchantRechargeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MerchantRechargeDao {

    @Autowired
    private MerchantRechargeMapper merchantRechargeMapper;

    public List<MerchantRecharge> selectMerchantRecharge(MerchantRecharge merchantRecharge){
       return merchantRechargeMapper.selectMerchantRecharge(merchantRecharge);
    }

    public void insertMerchantRecharge(MerchantRecharge merchantRecharge){
        merchantRechargeMapper.insert(merchantRecharge);
    }
}