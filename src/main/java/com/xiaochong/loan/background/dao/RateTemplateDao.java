package com.xiaochong.loan.background.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaochong.loan.background.entity.po.RateTemplate;
import com.xiaochong.loan.background.mapper.RateTemplateMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("rateTemplateDao")
public class RateTemplateDao {

    @Resource
    private RateTemplateMapper rateTemplateMapper;


    public int insert(RateTemplate rateTemplate){
        if(rateTemplate.getCreateTime()==null){
            rateTemplate.setCreateTime(new Date());
        }
        return rateTemplateMapper.insert(rateTemplate);
    }


    public Page<RateTemplate> selectAllPage(Integer pageNum,Integer pageSize,Integer merId){
        PageHelper.startPage(pageNum, pageSize);
        List<RateTemplate> rateTemplates = rateTemplateMapper.selectByMerId(merId);
        return (Page<RateTemplate>)rateTemplates;
    }

    public RateTemplate selectRateTemplateById(Integer id){
        return rateTemplateMapper.selectByPrimaryKey(id);
    }

    public List<RateTemplate> selectRateTemplate(RateTemplate rateTemplate){
        return  rateTemplateMapper.selectRateTemplate(rateTemplate);
    }

}
