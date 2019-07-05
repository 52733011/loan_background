package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportCallRecord;
import com.xiaochong.loan.background.mapper.ReportCallRecordMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportCallRecordDao")
public class ReportCallRecordDao {

    @Resource
    private ReportCallRecordMapper reportCallRecordMapper;


    public void insertOrUpdateByOrderNumAndPhone(ReportCallRecord reportCallRecord){
        ReportCallRecord oldReportCallRecord = reportCallRecordMapper.selectByOrderNumAndPhone(reportCallRecord.getOrderNo(), reportCallRecord.getPhone());
        if (Objects.nonNull(oldReportCallRecord)){
            reportCallRecord.setId(oldReportCallRecord.getId());
            reportCallRecordMapper.updateByPrimaryKeySelective(reportCallRecord);
        }else {
            reportCallRecordMapper.insert(reportCallRecord);
        }
    }

    public List<ReportCallRecord> selectByOrderNum(String orderNum){
        return reportCallRecordMapper.selectByOrderNum(orderNum);
    }
}
