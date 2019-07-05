package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchDataTemplate;
import com.xiaochong.loan.background.mapper.MerchDataTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/1.
 */
@Repository
public class MerchDataTemplateDao {

    @Autowired
    private MerchDataTemplateMapper merchDataTemplateMapper;


    public List<MerchDataTemplate> searchMerchDataTemplate(MerchDataTemplate merchDataTemplate){
       return merchDataTemplateMapper.searchMerchDataTemplate(merchDataTemplate);
    }


    public int insertTemplate(MerchDataTemplate merchDataTemplate){
       return merchDataTemplateMapper.insert(merchDataTemplate);
    }

    public void update(MerchDataTemplate merchDataTemplate){
        merchDataTemplateMapper.updateByPrimaryKeySelective(merchDataTemplate);
    }

    public MerchDataTemplate selectById(Integer templateId) {
        return merchDataTemplateMapper.selectByPrimaryKey(templateId);
    }
}
