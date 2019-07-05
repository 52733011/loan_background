package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportContactRecord;
import com.xiaochong.loan.background.mapper.ReportContactRecordMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportContactRecord")
public class ReportContactRecordDao {


    @Resource
    private ReportContactRecordMapper reportContactRecordMapper;

    public void insertOrUpdateOrderNumAndPhone(ReportContactRecord reportContactRecord){
        ReportContactRecord oldReportContactRecord = reportContactRecordMapper.selectByOrderNumAndPhone(reportContactRecord.getOrderNo(), reportContactRecord.getPhone());
        if (Objects.nonNull(oldReportContactRecord)){
            reportContactRecord.setId(oldReportContactRecord.getId());
            reportContactRecordMapper.updateByPrimaryKeySelective(reportContactRecord);
        }else {
            reportContactRecordMapper.insert(reportContactRecord);
        }
    }

    public List<ReportContactRecord> selectByOrderNum(String orderNum){
        return reportContactRecordMapper.selectByOrderNum(orderNum);
    }
}
