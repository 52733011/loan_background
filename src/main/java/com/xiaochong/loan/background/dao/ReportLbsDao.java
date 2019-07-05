package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportLbs;
import com.xiaochong.loan.background.mapper.ReportLbsMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("reportLbsDao")
public class ReportLbsDao {

    @Resource
    private ReportLbsMapper reportLbsMapper;


    public int insertOrUpdateByOrderNum(ReportLbs reportLbs) {
        ReportLbs searchReportLbs = new ReportLbs();
        searchReportLbs.setOrderNo(reportLbs.getOrderNo());
        searchReportLbs = reportLbsMapper.getByReportLbs(reportLbs);
        if(searchReportLbs==null){
            return this.insert(reportLbs);
        }else{
            searchReportLbs.setFiledName(reportLbs.getFiledName());
            searchReportLbs.setFiledValue(reportLbs.getFiledValue());
            searchReportLbs.setFiledDesc(reportLbs.getFiledDesc());
            searchReportLbs.setCreateTime(new Date());
            return this.update(reportLbs);
        }
    }

    public int update(ReportLbs reportLbs) {
        return reportLbsMapper.updateByPrimaryKeySelective(reportLbs);
    }

    public int insert(ReportLbs reportLbs) {
        if(reportLbs.getCreateTime()==null){
            reportLbs.setCreateTime(new Date());
        }
        return reportLbsMapper.insertSelective(reportLbs);
    }

    public ReportLbs getByReportLbs(ReportLbs reportLbs) {
        return reportLbsMapper.getByReportLbs(reportLbs);
    }

    public List<ReportLbs> listByReportLbs(ReportLbs reportLbs) {
        return reportLbsMapper.listByReportLbs(reportLbs);
    }
}
