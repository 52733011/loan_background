package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.BankCard;
import com.xiaochong.loan.background.mapper.BankCardMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("bankCardDao")
public class BankCardDao {

    @Resource
    private BankCardMapper bankCardMapper;

    public BankCard getByBankCard(BankCard bankCard) {
        return bankCardMapper.getByBankCard(bankCard);
    }

    public int insert(BankCard bankCard) {
        return bankCardMapper.insertSelective(bankCard);
    }

    public int update(BankCard bankCard) {
        return bankCardMapper.updateByPrimaryKeySelective(bankCard);
    }

    public List<BankCard> selectByBankCard(BankCard bankCardForSearch) {
       return bankCardMapper.selectByBankCard(bankCardForSearch);
    }
}
