package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportYysReportInfo;
import com.xiaochong.loan.background.mapper.ReportYysReportInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

@Repository("reportYysReportDao")
public class ReportYysReportDao {

    @Resource
    private ReportYysReportInfoMapper reportYysReportInfoMapper;

    public void inserOrUpdateByNameAndOrderNum(ReportYysReportInfo reportYysReportInfo){
        ReportYysReportInfo oldReportYysReportInfo = reportYysReportInfoMapper.selectByOrderNumAndName(reportYysReportInfo.getOrderNo(), reportYysReportInfo.getFiledName());
        if (Objects.nonNull(oldReportYysReportInfo)){
            reportYysReportInfo.setId(oldReportYysReportInfo.getId());
            reportYysReportInfoMapper.updateByPrimaryKeySelective(reportYysReportInfo);
        }else {
            reportYysReportInfoMapper.insert(reportYysReportInfo);
        }
    }

    public ReportYysReportInfo selectByFiledNameAndOrderNum(String orderNum,String filedNmae){
        return reportYysReportInfoMapper.selectByOrderNumAndName(orderNum,filedNmae);
    }

}
