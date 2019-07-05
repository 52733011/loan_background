package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.BorrowerBalance;
import com.xiaochong.loan.background.mapper.BorrowerBalanceMapper;
import com.xiaochong.loan.background.mapper.BorrowerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by jinxin on 2017/9/9.
 */
@Repository
public class BorrowerBalanceDao {

    @Autowired
    private BorrowerBalanceMapper borrowerBalanceMapper;

    public BorrowerBalance selectBorrowerBalanceById(Integer id){
        return borrowerBalanceMapper.selectByPrimaryKey(id);
    }

    public BorrowerBalance selectBorrowerBalanceByBorrowerId(Integer borrowerId){
        return borrowerBalanceMapper.selectBorrowerBalanceByBorrowerId(borrowerId);
    }

    public void insert(BorrowerBalance borrowerBalance) {
        borrowerBalanceMapper.insert(borrowerBalance);
    }

    public void update(BorrowerBalance borrowerBalance) {
        borrowerBalanceMapper.updateByPrimaryKeySelective(borrowerBalance);
    }
}
