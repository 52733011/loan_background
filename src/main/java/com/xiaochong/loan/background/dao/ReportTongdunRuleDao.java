package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportTongdunRule;
import com.xiaochong.loan.background.mapper.ReportTongdunRuleMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportTongdunRuleDao")
public class ReportTongdunRuleDao {


    @Resource
    private ReportTongdunRuleMapper reportTongdunRuleMapper;


    public void insertOrUpdateByOrderNum(ReportTongdunRule reportTongdunRule){
        ReportTongdunRule oldReportTongdunRule = reportTongdunRuleMapper.selectByOrderNumAndItemName(reportTongdunRule.getOrderNo(), reportTongdunRule.getItemname());
        if (Objects.nonNull(oldReportTongdunRule)){
            reportTongdunRule.setId(oldReportTongdunRule.getId());
            reportTongdunRuleMapper.updateByPrimaryKeySelective(reportTongdunRule);
        }else {
            reportTongdunRuleMapper.insert(reportTongdunRule);
        }
    }


    public void updateSelective(ReportTongdunRule reportTongdunRule){
        reportTongdunRuleMapper.updateByPrimaryKeySelective(reportTongdunRule);
    }

    public List<ReportTongdunRule> selectByOrderNum(String orderNum){
        return reportTongdunRuleMapper.selectByOrderNum(orderNum);
    }

}
