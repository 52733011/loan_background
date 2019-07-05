package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.CountNum;
import com.xiaochong.loan.background.mapper.CountNumMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("CountNumDao")
public class CountNumDao {

    @Resource
    private CountNumMapper countNumMapper;

    public int insert(CountNum countNum){
        return countNumMapper.insert(countNum);
    }

    public CountNum queryById(Integer id){
        return countNumMapper.selectByPrimaryKey(id);
    }

    public CountNum getByCountNum(CountNum countNum){
        return countNumMapper.getByCountNum(countNum);
    }

    public int updateByCompany(CountNum countNum){
        return countNumMapper.updateByPrimaryKeySelective(countNum);
    }

    public int cleanAllValue(){
        return countNumMapper.cleanAllValue();
    }

}
