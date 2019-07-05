package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.Report;
import com.xiaochong.loan.background.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * Created by jinxin on 2017/8/18.
 */
@Repository("reportDao")
public class ReportDao {

    @Autowired
    private ReportMapper reportMapper;

    public Report selectByOrderNo(String orderNo){
        return  reportMapper.selectByOrderNo(orderNo);
    }

    public void insertOrUpdateByOrderNum(Report report){
        Report oldReport = reportMapper.selectByOrderNo(report.getOrderNo());
        if (Objects.nonNull(oldReport)){
            report.setId(oldReport.getId());
            reportMapper.updateByPrimaryKeySelective(report);
        }else {
            reportMapper.insert(report);
        }
    }
}
