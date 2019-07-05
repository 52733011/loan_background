package com.xiaochong.loan.background.component;

import com.xiaochong.loan.background.dao.BorrowerBalanceDao;
import com.xiaochong.loan.background.entity.po.BorrowerBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jinxin on 2017/10/17.
 */
@Component
public class BorrowerBanlanceComponent {

    public static final Logger logger = LoggerFactory.getLogger(BorrowerBanlanceComponent.class);

    @Autowired
    private BorrowerBalanceDao borrowerBalanceDao;

    Lock lock =  new ReentrantLock();

    public Map<String,Object> updateBalance(BigDecimal money, Integer operator, Integer borrowerId){
        Map<String,Object> map = new HashMap<>();
        map.put("result","fail");
        try{
            lock.lock();
            BorrowerBalance borrowerBalance = borrowerBalanceDao.selectBorrowerBalanceByBorrowerId(borrowerId);
            BigDecimal balance = borrowerBalance.getBalance();
            map.put("before",balance);
            BigDecimal result = balance.add(money);
            if(result.compareTo(BigDecimal.ZERO)<0){
                map.put("result","less");
                return map;
            }
            map.put("after",result);
            BorrowerBalance borrowerBalanceForUpdate = new BorrowerBalance();
            borrowerBalanceForUpdate.setUpdateTime(new Date());
            borrowerBalanceForUpdate.setUpdateBy(operator);
            borrowerBalanceForUpdate.setBalance(result);
            borrowerBalanceForUpdate.setId(borrowerBalance.getId());
            borrowerBalanceDao.update(borrowerBalanceForUpdate);
            map.put("result","success");
        }catch (Exception e){
            logger.error("余额变动失败！",e);
            return  map;
        }finally {
            lock.unlock();
        }
        return map;
    }
}
