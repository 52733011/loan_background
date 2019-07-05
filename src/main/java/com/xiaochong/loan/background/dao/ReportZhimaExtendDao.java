package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ReportZhimaExtend;
import com.xiaochong.loan.background.mapper.ReportZhimaExtendMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Repository("reportZhimaExtendDao")
public class ReportZhimaExtendDao {

    @Resource
    private ReportZhimaExtendMapper reportZhimaExtendMapper;

    public void insertOrUpdateByZhimaIdAndDesc(ReportZhimaExtend reportZhimaExtend){
        ReportZhimaExtend oldReportZhimaExtend = reportZhimaExtendMapper.selectZhimaIdAndDesc(reportZhimaExtend.getZhimaId(), reportZhimaExtend.getDescription());
        if (Objects.nonNull(oldReportZhimaExtend)){
            reportZhimaExtend.setId(reportZhimaExtend.getZhimaId());
            reportZhimaExtendMapper.updateByPrimaryKeySelective(reportZhimaExtend);
        }else {
            reportZhimaExtendMapper.insert(reportZhimaExtend);
        }
    }

    public List<ReportZhimaExtend> selectByZhimaId(Integer zhimaId){
        return reportZhimaExtendMapper.selectZhimaId(zhimaId);
    }

}
