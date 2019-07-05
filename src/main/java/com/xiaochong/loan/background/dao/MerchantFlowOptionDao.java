package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchantFlowOption;
import com.xiaochong.loan.background.mapper.MerchantFlowOptionMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created by jinxin on 2017/8/11.
 */
@Repository("merchantFlowOptionDao")
public class MerchantFlowOptionDao {

    @Autowired
    private MerchantFlowOptionMapper merchantFlowOptionMapper;

    public List<MerchantFlowOption> selectMerchantFlowOption(MerchantFlowOption merchantFlowOption){
        return  merchantFlowOptionMapper.selectMerchantFlowOption(merchantFlowOption);
    }

    public void insertMerchantFlowOption(MerchantFlowOption merchantFlowOption){
        List<MerchantFlowOption> merchantFlowOptions = merchantFlowOptionMapper.selectMerchantFlowOption(merchantFlowOption);
        if(CollectionUtils.isEmpty(merchantFlowOptions)){
            merchantFlowOptionMapper.insert(merchantFlowOption);
        }
    }

    public int deleteByMerchIdAndFlowNo(MerchantFlowOption merchantFlowOption) {
       return merchantFlowOptionMapper.deleteByMerchIdAndFlowNo(merchantFlowOption);
    }

    public void deleteByMerchId(Integer merchId) {
        merchantFlowOptionMapper.deleteByMerchId(merchId);
    }
}
