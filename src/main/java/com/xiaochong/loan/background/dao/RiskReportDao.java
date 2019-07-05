package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.RiskReport;
import com.xiaochong.loan.background.mapper.RiskReportMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("riskReportDao")
public class RiskReportDao {

    @Resource
    private RiskReportMapper riskReportMapper;

    public void insert(RiskReport riskReport){
        riskReportMapper.insert(riskReport);
    }

}
