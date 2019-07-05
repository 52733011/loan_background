package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportLoanInfo;
import com.xiaochong.loan.background.mapper.ReportLoanInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportLoanInfoDao")
public class ReportLoanInfoDao {

    @Resource
    private ReportLoanInfoMapper reportLoanInfoMapper;

    public void insertOrUpdateByOrderNumAndName(ReportLoanInfo reportLoanInfo){
        ReportLoanInfo OldReportLoanInfo = reportLoanInfoMapper.selectByRuleIdAndName(reportLoanInfo.getRuleId(), reportLoanInfo.getName());
        if (Objects.nonNull(OldReportLoanInfo)){
            reportLoanInfo.setId(OldReportLoanInfo.getId());
            reportLoanInfoMapper.updateByPrimaryKey(reportLoanInfo);
        }else {
            reportLoanInfoMapper.insert(reportLoanInfo);
        }
    }

    public List<ReportLoanInfo> selectByRuleId(Integer ruleId){
        return reportLoanInfoMapper.selectByRuleId(ruleId);
    }

}
