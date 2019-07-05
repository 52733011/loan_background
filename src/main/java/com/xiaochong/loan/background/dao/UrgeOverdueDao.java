package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.UrgeOverdue;
import com.xiaochong.loan.background.mapper.UrgeOverdueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2017/10/23.
 */
@Repository
public class UrgeOverdueDao {

    @Autowired
    private UrgeOverdueMapper urgeOverdueMapper;

    public void insert(UrgeOverdue urgeOverdue){
        urgeOverdueMapper.insert(urgeOverdue);
    }

    public void  update(UrgeOverdue urgeOverdue){
        urgeOverdueMapper.updateByPrimaryKeySelective(urgeOverdue);
    }

    public List<UrgeOverdue> selectByUrgeOverDue(UrgeOverdue urgeOverdue){
        return urgeOverdueMapper.selectByUrgeOverDue(urgeOverdue);
    }

    public UrgeOverdue selectByBorrowerId(Integer borrowerId) {
        return urgeOverdueMapper.selectByBorrowerId();
    }

    public List<Integer> selectBorrowerIdsByStatus(String status) {
        return urgeOverdueMapper.selectBorrowerIdsByStatus(status);
    }

    public void updateByBorrowerId(UrgeOverdue urgeOverdueForUpdate) {
        urgeOverdueMapper.updateByBorrowerId(urgeOverdueForUpdate);
    }

    public List<Map<String,Object>> selectCountByUrgeOverdue(UrgeOverdue urgeOverdue) {
        return  urgeOverdueMapper.selectCountByUrgeOverdue(urgeOverdue);
    }

    public UrgeOverdue selectByApplicationId(Integer applicationId) {
        return  urgeOverdueMapper.selectByApplicationId(applicationId);
    }

    public List<Integer> selectApplicationIdsByStatus(String status) {
        return  urgeOverdueMapper.selectApplicationIdsByStatus(status);
    }

    public void updateByApplicationId(UrgeOverdue urgeOverdueForUpdate) {
        urgeOverdueMapper.updateByApplicationId(urgeOverdueForUpdate);
    }
}
