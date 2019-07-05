package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportEducationInfo;
import com.xiaochong.loan.background.entity.po.ReportUserInfo;
import com.xiaochong.loan.background.mapper.ReportEducationInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Objects;

@Repository("reportEducationInfoDao")
public class ReportEducationInfoDao {

    @Resource
    private ReportEducationInfoMapper reportEducationInfoMapper;

    public void insertOrUpdateByOrderNum(ReportEducationInfo reportEducationInfo){
        ReportEducationInfo oldReportEducation = reportEducationInfoMapper.selectReportUserByOrderNumAndFiledName(reportEducationInfo.getOrderNo(), reportEducationInfo.getFiledName());
        if (Objects.nonNull(oldReportEducation)){
            reportEducationInfo.setId(oldReportEducation.getId());
            reportEducationInfoMapper.updateByPrimaryKeySelective(reportEducationInfo);
        }else {
            reportEducationInfoMapper.insert(reportEducationInfo);
        }
    }

    public ReportEducationInfo selectByOrderNumAndFiledName(String orderNum, String filedName){
        return reportEducationInfoMapper.selectReportUserByOrderNumAndFiledName(orderNum,filedName);
    }


}
