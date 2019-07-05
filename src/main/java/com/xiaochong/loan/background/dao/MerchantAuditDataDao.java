package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchantAuditData;
import com.xiaochong.loan.background.mapper.MerchantAuditDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/6.
 */
@Repository
public class MerchantAuditDataDao {

    @Autowired
    private MerchantAuditDataMapper merchantAuditDataMapper;

    public List<MerchantAuditData> selectMerchantAuditData(MerchantAuditData merchantAuditData){
        return  merchantAuditDataMapper.selectMerchantAuditData(merchantAuditData);
    }
    public List<MerchantAuditData> selectMerchantAuditDataByApplicationId(Integer applicationId){
        return  merchantAuditDataMapper.selectMerchantAuditDataByApplicationId(applicationId);
    }

    public void insertMerchantAuditData(MerchantAuditData merchantAuditData) {
        merchantAuditDataMapper.insert(merchantAuditData);
    }
}
