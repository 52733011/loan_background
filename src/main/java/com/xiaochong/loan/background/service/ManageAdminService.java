package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Constants;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.ManageAdminDao;
import com.xiaochong.loan.background.dao.RoleManageDao;
import com.xiaochong.loan.background.dao.UserRoleManageDao;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.po.UserRoleManage;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.ManagerAdminVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.IsTypeEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by jinxin on 2017/8/14.
 * 后台登录管理
 */
@Service
public class ManageAdminService extends BaseService{

    private Logger logger = LoggerFactory.getLogger(RoleManageService.class);

    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private ManageAdminDao manageAdminDao;

    @Resource(name = "roleManageDao")
    private RoleManageDao roleManageDao;

    @Resource(name = "userRoleManageDao")
    private UserRoleManageDao userRoleManageDao;

    /**
     * 后台登录
     * @param userName
     * @param password
     * @param kaptchacode
     * @param time
     * @return token
     */
    public BusinessVo<String> managerLogin(String userName, String password, String kaptchacode, String time){
        BusinessVo<String> businessVo = new BusinessVo<>();
        String kaptchaExpected = sessionComponent.getAttribute(Constants.KAPTCHA_SESSION_KEY+time);
        if (!kaptchacode.equals(kaptchaExpected)){
            businessVo.setCode(BusinessConstantsUtils.KAPTCHA_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.KAPTCHA_ERROR_DESC);
            return businessVo;
        }
        ManageAdmin manageAdmin = new ManageAdmin();
        manageAdmin.setUserName(userName);
        manageAdmin.setPassword(MD5Utils.MD5Encode(password));
        manageAdmin = manageAdminDao.getByManageAdmin(manageAdmin);
        String token = "";
        if (null != manageAdmin){
            token = CreateTokenUtil.createToken();
            manageAdmin.setLastLoginTime(new Date());
            manageAdminDao.updateManageAdmin(manageAdmin);
            sessionComponent.setAttribute(UserLoginTypeEnum.MANAGE.getType() + "-" + token, manageAdmin);
        }else {
            businessVo.setCode(BusinessConstantsUtils.USERNAME_PASSWORD_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.USERNAME_PASSWORD_ERROR_DESC);
            return businessVo;
        }
        businessVo.setData(token);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    public BusinessVo<Boolean> addManagerAdmin(String userName, String password, String phone, Integer roleId) throws DataDisposeException {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        ManageAdmin manageAdmin = new ManageAdmin();
        manageAdmin.setPhone(phone);
        manageAdmin =  manageAdminDao.getByManageAdmin(manageAdmin);
        if(manageAdmin!=null){
            businessVo.setCode(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_DESC);
            logger.warn("manageAdminService.addManagerAdmin手机号已经存在！manageAdmin:{}",manageAdmin);
            return businessVo;
        }
        manageAdmin = new ManageAdmin();
        manageAdmin.setPhone(phone);
        manageAdmin.setUserName(userName);
        manageAdmin.setPassword(MD5Utils.MD5Encode(password));
        manageAdmin.setStatus(IsTypeEnum.TRUE.getType());
        if( manageAdminDao.insert(manageAdmin)!=1){
            businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
            businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
            logger.warn("manageAdminService.addManagerAdmin！manageAdmin:{}",manageAdmin);
            return businessVo;
        }
        UserRoleManage userRoleManage = new UserRoleManage();
        userRoleManage.setRoleId(roleId);
        userRoleManage.setUserId(manageAdmin.getId());
        if( userRoleManageDao.insert(userRoleManage)!=1){
            logger.error("后台分配角色失败！userRoleManage:{}",userRoleManage);
            throw new DataDisposeException("后台分配角色失败！");
        }
        return businessVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<Boolean> updateManagerAdminRole(Integer userId, Integer roleId, String status) throws DataDisposeException {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        ManageAdmin manageAdmin = manageAdminDao.getById(userId);
        if(manageAdmin==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("manageAdminService.updateManagerAdminRole用户不存在！manageAdminId:{}",userId);
            return businessVo;
        }
        if(StringUtils.isNotBlank(status)){
            manageAdmin.setStatus(status);
            if(manageAdminDao.updateManageAdmin(manageAdmin)!=1){
                logger.error("更新用户状态失败！userRoleManage:{}",manageAdmin);
                throw new DataDisposeException("更新用户状态失败！");
            }
        }
        UserRoleManage userRoleManage = new UserRoleManage();
        userRoleManage.setUserId(userId);
        userRoleManage = userRoleManageDao.getByUserRoleManage(userRoleManage);
        if(userRoleManage==null){
            userRoleManage = new UserRoleManage();
        }
        userRoleManage.setUserId(userId);
        userRoleManage.setRoleId(roleId);
        if(userRoleManageDao.insertOrUpdate(userRoleManage)!=1){
            logger.error("更新用户权限失败！userRoleManage:{}",userRoleManage);
            throw new DataDisposeException("更新用户权限失败！");
        }
        businessVo.setData(true);
        return businessVo;
    }


    public BusinessVo<BasePageInfoVo<ManagerAdminVo>> manageAdminPage(Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<ManagerAdminVo>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        PageHelper.startPage(pageNum, pageSize,true);
        Page<ManagerAdminVo> list =
                (Page<ManagerAdminVo>)manageAdminDao.manageAdminPage();

        //拼装成返回类
        PageInfo<ManagerAdminVo> pageInfo = list.toPageInfo();
        //拼装分页类
        BasePageInfoVo<ManagerAdminVo> basePageInfoVo = assemblyBasePageInfo(pageInfo);
        basePageInfoVo.setResultList(pageInfo.getList());
        businessVo.setData(basePageInfoVo);
        return businessVo;
    }
}
