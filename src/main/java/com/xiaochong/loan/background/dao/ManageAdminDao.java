package com.xiaochong.loan.background.dao;

import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.vo.ManagerAdminVo;
import com.xiaochong.loan.background.mapper.ManageAdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by jinxin on 2017/8/14.
 */
@Repository("manageAdminDao")
public class ManageAdminDao {

    @Autowired
    private ManageAdminMapper manageAdminMapper;

    public  int  updateManageAdmin(ManageAdmin manageAdmin){
        return manageAdminMapper.updateByPrimaryKeySelective(manageAdmin);
    }


    public ManageAdmin getById(Integer id) {
        return   manageAdminMapper.selectByPrimaryKey(id);
    }

    public ManageAdmin getByManageAdmin(ManageAdmin manageAdmin) {
        return manageAdminMapper.getByManageAdmin(manageAdmin);
    }

    public int insert(ManageAdmin manageAdmin) {
        if(manageAdmin.getCreatetime()==null){
            manageAdmin.setCreatetime(new Date());
        }
        return manageAdminMapper.insertSelective(manageAdmin);
    }

    public List<ManagerAdminVo> manageAdminPage() {
        return manageAdminMapper.manageAdminPage();
    }
}
