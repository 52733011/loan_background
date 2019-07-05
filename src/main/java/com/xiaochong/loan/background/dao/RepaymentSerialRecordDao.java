package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.RepaymentSerialRecord;
import com.xiaochong.loan.background.mapper.RepaymentSerialRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2017/10/18.
 */
@Repository
public class RepaymentSerialRecordDao {

    @Autowired
    private RepaymentSerialRecordMapper repaymentSerialRecordMapper;

    public void insert(RepaymentSerialRecord repaymentSerialRecord){
        repaymentSerialRecordMapper.insert(repaymentSerialRecord);
    }

    public void update(RepaymentSerialRecord repaymentSerialRecord){
        repaymentSerialRecordMapper.updateByPrimaryKeySelective(repaymentSerialRecord);
    }

    public List<RepaymentSerialRecord> selectByRepaymentSerialRecord(RepaymentSerialRecord repaymentSerialRecord){
       return repaymentSerialRecordMapper.selectByRepaymentSerialRecord(repaymentSerialRecord);
    }

    public List<Map<String, Object>> selectCountByRepaymentSerialRecord(RepaymentSerialRecord repaymentSerialRecord){
        return  repaymentSerialRecordMapper.selectCountByRepaymentSerialRecord(repaymentSerialRecord);
    }

    public RepaymentSerialRecord selectById(Integer id){
        return  repaymentSerialRecordMapper.selectByPrimaryKey(id);
    }

    public String selectStatusById(Integer id){
        return  repaymentSerialRecordMapper.selectStatusById(id);
    }


    public void updateBorrowerIdById(RepaymentSerialRecord repaymentSerialRecordForUpdate) {
        repaymentSerialRecordMapper.updateBorrowerIdById(repaymentSerialRecordForUpdate);
    }
}
