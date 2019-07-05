package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.mapper.LoanApplicationMapper;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/5.
 */
@Repository
public class LoanApplicationDao {

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;


    public List<LoanApplication> selectLoanApplication(LoanApplication loanApplication){
        return  loanApplicationMapper.selectLoanApplication(loanApplication);
    }

    public List<LoanApplication> selectLoanAuditApplicationListByStutus(LoanApplication loanApplication){
        return  loanApplicationMapper.selectLoanAuditApplicationListByStutus(loanApplication);
    }

    public LoanApplication selectLoanApplicationById(Integer applicationId) {
        return loanApplicationMapper.selectByPrimaryKey(applicationId);
    }

    public void updateById(LoanApplication loanApplication) {
        loanApplicationMapper.updateByPrimaryKeySelective(loanApplication);
    }

    public void insert(LoanApplication loanApplication) {
        loanApplicationMapper.insertSelective(loanApplication);
    }

    public int selectCountByOrderNo(String orderNo) {
       return loanApplicationMapper.selectCountByOrderNo(orderNo);
    }
}
