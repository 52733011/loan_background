package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportLbsEvtInfo;
import com.xiaochong.loan.background.mapper.ReportLbsEvtInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("reportLbsEvtInfoDao")
public class ReportLbsEvtInfoDao {

    @Resource
    private ReportLbsEvtInfoMapper reportLbsEvtInfoMapper;

    public List<ReportLbsEvtInfo> listByReportLbsEvtInfo(ReportLbsEvtInfo reportLbsEvtInfo) {
        return reportLbsEvtInfoMapper.listByReportLbsEvtInfo(reportLbsEvtInfo);
    }

    public int deleteByReportLbsEvtInfo(ReportLbsEvtInfo reportLbsEvtInfo) {
        return reportLbsEvtInfoMapper.deleteByPrimaryKey(reportLbsEvtInfo.getId());
    }

    public int insert(ReportLbsEvtInfo reportLbsEvtInfo) {
        if(reportLbsEvtInfo.getCreateTime()==null){
            reportLbsEvtInfo.setCreateTime(new Date());
        }
        return reportLbsEvtInfoMapper.insertSelective(reportLbsEvtInfo);
    }
}
