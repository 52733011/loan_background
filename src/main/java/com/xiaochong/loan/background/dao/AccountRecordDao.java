package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.AccountRecord;
import com.xiaochong.loan.background.mapper.AccountRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/8/15.
 */
@Repository
public class AccountRecordDao {

    @Autowired
    private AccountRecordMapper accountRecordMapper;

    public List<AccountRecord> selectAccountRecord(AccountRecord accountRecord){
        return  accountRecordMapper.selectAccountRecord(accountRecord);
    }

    public void insertAccountRescord(AccountRecord accountRecord){
        accountRecordMapper.insert(accountRecord);
    }

    public void updateAccountRecord(AccountRecord accountRecord){
        accountRecordMapper.updateByPrimaryKeySelective(accountRecord);
    }

    public void updateAccountRecordByOrderNo(AccountRecord accountRecord){
        accountRecordMapper.updateAccountRecordByOrderNo(accountRecord);
    }

    public int getCountForAccountRecord(AccountRecord accountRecord) {
        return  accountRecordMapper.getCountForAccountRecord(accountRecord);
    }
}
