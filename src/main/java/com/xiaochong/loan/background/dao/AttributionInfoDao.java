package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.AttributionInfo;
import com.xiaochong.loan.background.mapper.AttributionInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("attributionInfoDao")
public class AttributionInfoDao {

    @Resource
    private AttributionInfoMapper attributionInfoMapper;

    public int insert(AttributionInfo attributionInfo) {
        return attributionInfoMapper.insertSelective(attributionInfo);
    }

    public void insertByBatch(List<AttributionInfo> attributionInfos) {
        attributionInfoMapper.insertByBatch(attributionInfos);
    }

    public AttributionInfo getByAttributionInfo(AttributionInfo attributionInfo) {
        return attributionInfoMapper.getByAttributionInfo(attributionInfo);
    }

    public List<AttributionInfo> listByAttributionInfo(AttributionInfo attributionInfo) {
        return attributionInfoMapper.listByAttributionInfo(attributionInfo);
    }
}
