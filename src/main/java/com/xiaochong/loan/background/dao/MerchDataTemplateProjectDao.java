package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchDataTemplateProject;
import com.xiaochong.loan.background.entity.po.MerchantSubmitProject;
import com.xiaochong.loan.background.mapper.MerchDataTemplateProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/1.
 */
@Repository
public class MerchDataTemplateProjectDao {

    @Autowired
    private MerchDataTemplateProjectMapper merchDataTemplateProjectMapper;

    public void insertProject(MerchDataTemplateProject merchDataTemplateProject){
        merchDataTemplateProjectMapper.insert(merchDataTemplateProject);
    }


    public List<MerchDataTemplateProject> selectProjectByTemplateId(Integer templateId) {
       return merchDataTemplateProjectMapper.selectProjectByTemplateId(templateId);
    }

    public MerchDataTemplateProject selectProjectById(Integer id) {
       return merchDataTemplateProjectMapper.selectByPrimaryKey(id);
    }


    public int selectCountProjectByTemplateId(Integer tempId) {
        return  merchDataTemplateProjectMapper.selectCountProjectByTemplateId(tempId);
    }
}
