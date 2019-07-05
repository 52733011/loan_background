package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.AuthLog;
import com.xiaochong.loan.background.mapper.AuthLogMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("authLogDao")
public class AuthLogDao {


    @Resource
    private AuthLogMapper authLogMapper;

    public AuthLog findLoanAuthByCardAndType(String idCard,String type){
        return authLogMapper.findLoanAuthByCardAndType(idCard,type);
    }


    public int updateSelective(AuthLog authLog){
        return authLogMapper.updateByPrimaryKeySelective(authLog);
    }

    public int insertAuthLog(AuthLog authLog){
        return authLogMapper.insert(authLog);
    }


    public AuthLog getByAuthLog(AuthLog authLog) {
        return authLogMapper.getByAuthLog(authLog);
    }

    public List<AuthLog> listByAuthLog(AuthLog authLog) {
        return authLogMapper.listByAuthLog(authLog);
    }
}
