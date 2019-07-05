package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportUserInfo;
import com.xiaochong.loan.background.mapper.ReportUserInfoMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Repository("reportUserInfoDao")
public class ReportUserInfoDao {

    @Resource
    private ReportUserInfoMapper reportUserInfoMapper;


    public void insertOrUpdateByOrderNum(ReportUserInfo reportUserInfo){
        ReportUserInfo oldReportUser = reportUserInfoMapper.selectReportUserByOrderNumAndFiledName(reportUserInfo.getOrderNo(),reportUserInfo.getFiledName());
        if (Objects.nonNull(oldReportUser)){
            reportUserInfo.setId(oldReportUser.getId());
            reportUserInfo.setCreateTime(new Date());
            reportUserInfoMapper.updateByPrimaryKeySelective(reportUserInfo);
        }else {
            reportUserInfoMapper.insert(reportUserInfo);
        }
    }


    public ReportUserInfo selectByOrderNumAndFiledName(String orderNum,String filedName){
        return reportUserInfoMapper.selectReportUserByOrderNumAndFiledName(orderNum,filedName);
    }

}
