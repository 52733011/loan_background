package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.MerchantSubmitData;
import com.xiaochong.loan.background.entity.po.MerchantSubmitProject;
import com.xiaochong.loan.background.mapper.MerchantSubmitDataMapper;
import com.xiaochong.loan.background.mapper.MerchantSubmitProjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jinxin on 2017/9/4.
 */
@Repository
public class MerchantSubmitProjectDao {

    @Autowired
    private MerchantSubmitProjectMapper merchantSubmitProjectMapper;

    public void insertMerchantSubmitProject(MerchantSubmitProject merchantSubmitProject){
        merchantSubmitProjectMapper.insert(merchantSubmitProject);
    }

    public List<MerchantSubmitProject> selectMerchantSubmitProject(MerchantSubmitProject merchantSubmitProject){
        return  merchantSubmitProjectMapper.selectMerchantSubmitProject(merchantSubmitProject);
    }

    public void insertOrUpdateMerchantSubmitProject(MerchantSubmitProject merchantSubmitProject) {
        MerchantSubmitProject merchantSubmitProjectForSearch = new MerchantSubmitProject();
        merchantSubmitProjectForSearch.setApplicationId(merchantSubmitProject.getApplicationId());
        merchantSubmitProjectForSearch.setProjectId(merchantSubmitProject.getProjectId());
        List<MerchantSubmitProject> merchantSubmitProjects = merchantSubmitProjectMapper.selectMerchantSubmitProject(merchantSubmitProjectForSearch);
        if(CollectionUtils.isEmpty(merchantSubmitProjects)){
            merchantSubmitProjectMapper.insert(merchantSubmitProject);
        }else {
            merchantSubmitProject.setId(merchantSubmitProjects.get(0).getId());
            merchantSubmitProjectMapper.updateByPrimaryKeySelective(merchantSubmitProject);
        }
    }

    public void deleteById(Integer submitProjectId) {
        merchantSubmitProjectMapper.deleteByPrimaryKey(submitProjectId);
    }

    public MerchantSubmitProject selectMerchantSubmitProjectById(Integer submitProjectId) {
        return  merchantSubmitProjectMapper.selectByPrimaryKey(submitProjectId);
    }


    public int selectCountMerchantSubmitProjectByAppId(Integer applicationId) {
        return  merchantSubmitProjectMapper.selectCountMerchantSubmitProjectByAppId(applicationId);
    }
}
