package com.xiaochong.loan.background.dao;


import com.xiaochong.loan.background.entity.po.SmsTemplate;
import com.xiaochong.loan.background.entity.vo.SmaTemplateVo;
import com.xiaochong.loan.background.mapper.SmsTemplateMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository("smsTemplateDao")
public class SmsTemplateDao {

    @Resource
    private SmsTemplateMapper smsTemplateMapper;

    public int insertOrUpdate(SmsTemplate smsTemplate) {
        if(smsTemplate.getId()==null){
            return this.insert(smsTemplate);
        }else{
            return this.update(smsTemplate);
        }
    }

    public int update(SmsTemplate smsTemplate) {
        if(smsTemplate.getUpdatetime()==null){
            smsTemplate.setUpdatetime(new Date());
        }
        return smsTemplateMapper.updateByPrimaryKeySelective(smsTemplate);
    }

    public int insert(SmsTemplate smsTemplate) {
        if(smsTemplate.getCreatetime()==null){
            smsTemplate.setCreatetime(new Date());
        }
        return smsTemplateMapper.insertSelective(smsTemplate);
    }

    public SmsTemplate getBySmsTemplate(SmsTemplate smsTemplate) {
        return smsTemplateMapper.getBySmsTemplate(smsTemplate);
    }

    public List<SmaTemplateVo> smsTemplatePage(Integer userId) {
        return smsTemplateMapper.smsTemplatePage(userId);
    }

    public SmsTemplate getById(Integer id) {
        return smsTemplateMapper.selectByPrimaryKey(id);
    }
}
