package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.SmsSendLog;
import com.xiaochong.loan.background.mapper.SmsSendLogMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

@Repository("smsSendLogDao")
public class SmsSendLogDao {

    @Resource
    private SmsSendLogMapper smsSendLogMapper;

    public int insert(SmsSendLog smsSendLog) {
        if(smsSendLog.getCreatetime()==null){
            smsSendLog.setCreatetime(new Date());
        }
        return smsSendLogMapper.insertSelective(smsSendLog);
    }

    public long countByMerchIdMonth(Integer merchId) {
        return smsSendLogMapper.countByMerchIdMonth(merchId);
    }

    public long countByMerchIdAll(Integer merchId) {
        return smsSendLogMapper.countByMerchIdAll(merchId);
    }
}
