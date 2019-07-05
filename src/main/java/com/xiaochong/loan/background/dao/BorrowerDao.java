package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.Borrower;
import com.xiaochong.loan.background.mapper.BorrowerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/9.
 */
@Repository
public class BorrowerDao {

    @Autowired
    private BorrowerMapper borrowerMapper;

    public List<Integer> selectBorrowerId(Borrower borrower){
        return borrowerMapper.selectBorrowerId(borrower);
    }

    public List<Borrower> selectBorrower(Borrower borrower){
        return borrowerMapper.selectBorrower(borrower);
    }

    public List<Borrower> selectBorrowerByPhoneOrIdCard(Borrower borrower){
        return borrowerMapper.selectBorrowerByPhoneOrIdCard(borrower);
    }

    public Borrower selectBorrowerById(Integer id){
        return borrowerMapper.selectByPrimaryKey(id);
    }

    public void insertBorrower(Borrower borrower) {
          borrowerMapper.insert(borrower);
    }
}
