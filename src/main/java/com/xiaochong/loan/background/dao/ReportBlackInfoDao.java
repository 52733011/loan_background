package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportBlackInfo;
import com.xiaochong.loan.background.mapper.ReportBlackInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportBlackInfoDao")
public class ReportBlackInfoDao {


    @Resource
    private ReportBlackInfoMapper reportBlackInfoMapper;

    public void insertOrUpdateByOrderNumAndName(ReportBlackInfo reportBlackInfo){
        ReportBlackInfo oldReportBlackInfo = reportBlackInfoMapper.selectByRuleIdAndName(reportBlackInfo.getRuleId(), reportBlackInfo.getName());
        if (Objects.nonNull(oldReportBlackInfo)){
            reportBlackInfo.setId(oldReportBlackInfo.getId());
            reportBlackInfoMapper.updateByPrimaryKeySelective(reportBlackInfo);
        }else {
            reportBlackInfoMapper.insert(reportBlackInfo);
        }
    }

    public List<ReportBlackInfo> selectByRuleId(Integer ruleId){
        return reportBlackInfoMapper.selectByRuleId(ruleId);
    }

}
