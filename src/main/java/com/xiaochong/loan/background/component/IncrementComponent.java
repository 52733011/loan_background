package com.xiaochong.loan.background.component;


import com.xiaochong.loan.background.dao.CountNumDao;
import com.xiaochong.loan.background.entity.po.CountNum;
import com.xiaochong.loan.background.utils.enums.CountTypeEnum;
import com.xiaochong.loan.background.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by wujiaxing on 2017/5/24.
 * 编号记录
 */
@Service("incrementComponent")
public class IncrementComponent {

    private Logger logger = LoggerFactory.getLogger(IncrementComponent.class);


    @Resource(name = "CountNumDao")
    private CountNumDao countNumDao;

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;

    public String getNo(CountTypeEnum countTypeEnum){
        String number = DateUtils.getCurrentDateyymd();
        String countNo = this.getCountNo(countTypeEnum);
        number += countNo;
        return number;
    }

    /**
     * 获取自增编号
     *
     * @param countTypeEnum 编号类型
     * @return String
     */
    public String getCountNo(CountTypeEnum countTypeEnum){
        String token = "countType:"+countTypeEnum.getType();
        CountNum countNum = sessionComponent.getAttributeCount(token);//从缓存获取自增编号
        if(countNum != null){
            this.update(token,countNum);
            return this.getNo(countNum.getCountValue());
        }
        countNum = new CountNum();
        countNum.setCountType(countTypeEnum.getType());
        countNum= countNumDao.getByCountNum(countNum);
        if(countNum != null && countNum.getId()!=null){
            this.update(token,countNum);
            return this.getNo(countNum.getCountValue());
        }else{
            countNum = new CountNum();
            countNum.setCountValue(1);
            countNum.setCountType(countTypeEnum.getType());
            countNumDao.insert(countNum);
            sessionComponent.setAttribute(token,countNum);
            return this.getNo(countNum.getCountValue());
        }
    }

    /**
     * 重置编号
     */
    public void clean(){
        countNumDao.cleanAllValue();
    }

    private String getNo(int no){
        String countNo = no+"";
        int len = countNo.length();
        for (int i = 0; i < 4-len; i++) {
            countNo = "0"+countNo;
        }
        return countNo;
    }

    private void update(String token, CountNum countNum){
        countNum.setCountValue(countNum.getCountValue()+1);
        countNumDao.updateByCompany(countNum);
        sessionComponent.setAttribute(token,countNum);
    }

}
