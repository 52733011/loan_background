package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchantAccount;
import com.xiaochong.loan.background.mapper.MerchantAccountMapper;
import com.xiaochong.loan.background.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jinxin on 2017/8/15.
 */
@Repository
public class MerchantAccountDao {

    @Autowired
    private MerchantAccountMapper merchantAccountMapper;

    public MerchantAccount selectMerchantAccountByMerchId(Integer merchId){
        return  merchantAccountMapper.selectMerchantAccountByMerchId(merchId);
    }

    public void insertMerchantAccount(MerchantAccount merchantAccount){
        merchantAccountMapper.insert(merchantAccount);
    }

    public void updateMerchantAccount(MerchantAccount merchantAccount){
        merchantAccountMapper.updateByPrimaryKeySelective(merchantAccount);
    }

}
