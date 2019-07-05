package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.ReportZhimaInfo;
import com.xiaochong.loan.background.mapper.ReportZhimaInfoMapper;
import org.jsoup.select.Evaluator;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportZhimaInfoDao")
public class ReportZhimaInfoDao {


    @Resource
    private ReportZhimaInfoMapper reportZhimaInfoMapper;

    public void insertOrUpdateByOrderNoAndCode(ReportZhimaInfo reportZhimaInfo){
        ReportZhimaInfo oldReportZhimaInfo = reportZhimaInfoMapper.selectByOrderNumAndCode(reportZhimaInfo.getOrderNo(), reportZhimaInfo.getCode());
        if (Objects.nonNull(oldReportZhimaInfo)){
            reportZhimaInfo.setId(oldReportZhimaInfo.getId());
            reportZhimaInfoMapper.updateByPrimaryKey(reportZhimaInfo);
        }else {
            reportZhimaInfoMapper.insert(reportZhimaInfo);
        }
    }

    public List<ReportZhimaInfo> selectByOrderNum(String orderNum){
        return reportZhimaInfoMapper.selectByOrderNum(orderNum);
    }
}
