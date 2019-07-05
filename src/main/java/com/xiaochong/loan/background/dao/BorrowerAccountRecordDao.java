package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.BorrowerAccountRecord;
import com.xiaochong.loan.background.mapper.BorrowerAccountRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jinxin on 2017/10/17.
 */
@Repository
public class BorrowerAccountRecordDao {

    @Autowired
    private BorrowerAccountRecordMapper borrowerAccountRecordMapper;

    public void  insert(BorrowerAccountRecord borrowerAccountRecord){
        borrowerAccountRecordMapper.insert(borrowerAccountRecord);
    }


}
