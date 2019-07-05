package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.RepaymentPlan;
import com.xiaochong.loan.background.mapper.RepaymentPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2017/9/7.
 */
@Repository
public class RepaymentPlanDao {


    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;


    public List<RepaymentPlan> selectRepaymentPlan(RepaymentPlan repaymentPlan){
        return repaymentPlanMapper.selectRepaymentPlan(repaymentPlan);
    }

    public RepaymentPlan selectById(Integer id){
        return repaymentPlanMapper.selectByPrimaryKey(id);
    }
    public List<RepaymentPlan> selectRepaymentPlanRecord(RepaymentPlan repaymentPlan){
        return repaymentPlanMapper.selectRepaymentPlanRecord(repaymentPlan);
    }

    public List<RepaymentPlan> selectRepaymentPlanApplicationId(Integer applicationId){
        return repaymentPlanMapper.selectRepaymentPlanByApplicationId(applicationId);
    }

    public void insertRepaymentPlan(RepaymentPlan repaymentPlan) {
        repaymentPlanMapper.insert(repaymentPlan);
    }

    public int update(RepaymentPlan repaymentPlan) {
        if(repaymentPlan.getUpdateTime()==null){
            repaymentPlan.setUpdateTime(new Date());
        }
        return repaymentPlanMapper.updateByPrimaryKeySelective(repaymentPlan);
    }

    public List<RepaymentPlan> listByApplicationIdStatus(Integer applicationId, String status) {
        return repaymentPlanMapper.listByApplicationIdStatus(applicationId,status);
    }

    public List<RepaymentPlan> selectByLoanApplicationIds(List<Integer> loanApplicationIds) {
        return repaymentPlanMapper.selectByLoanApplicationIds(loanApplicationIds);

    }

    public List<RepaymentPlan> selectOverDueInfos() {
        return repaymentPlanMapper.selectOverDueInfos();
    }

    public List<Map<String,Object>> selectOverDueStatistics() {
        return repaymentPlanMapper.selectOverDueStatistics();
    }

    public List<RepaymentPlan> selectListByRepaymentPlanRecord(RepaymentPlan repaymentPlanForSearch) {
        return repaymentPlanMapper.selectListByRepaymentPlanRecord(repaymentPlanForSearch);
    }

    public Integer selectCountByRepaymentPlan(RepaymentPlan repaymentPlan) {
        return repaymentPlanMapper.selectCountByRepaymentPlan(repaymentPlan);
    }
}
