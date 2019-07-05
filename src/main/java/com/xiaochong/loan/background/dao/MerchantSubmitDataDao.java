package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchantSubmitData;
import com.xiaochong.loan.background.mapper.MerchantSubmitDataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/4.
 */
@Repository
public class MerchantSubmitDataDao {

    @Autowired
    private MerchantSubmitDataMapper merchantSubmitDataMapper;

    public void insertOrUpdateMerchantSubmitData(MerchantSubmitData merchantSubmitData){
        MerchantSubmitData merchantSubmitDataForSearch = new MerchantSubmitData();
        merchantSubmitDataForSearch.setApplicationId(merchantSubmitData.getApplicationId());
        List<MerchantSubmitData> merchantSubmitDataList = merchantSubmitDataMapper.selectMerchantSubmitData(merchantSubmitDataForSearch);
        if(CollectionUtils.isEmpty(merchantSubmitDataList)){
            merchantSubmitDataMapper.insert(merchantSubmitData);
        }else {
            merchantSubmitData.setId(merchantSubmitDataList.get(0).getId());
            merchantSubmitDataMapper.updateByPrimaryKeySelective(merchantSubmitData);
        }
    }

    public List<MerchantSubmitData> selectMerchantSubmitData(MerchantSubmitData merchantSubmitData){
        return  merchantSubmitDataMapper.selectMerchantSubmitData(merchantSubmitData);
    }
    public List<MerchantSubmitData> selectMerchantSubmitDataByApplicationId(Integer applicationId){
        return  merchantSubmitDataMapper.selectMerchantSubmitDataByApplicationId(applicationId);
    }


}
