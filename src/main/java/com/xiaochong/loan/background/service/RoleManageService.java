package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.RoleManageDao;
import com.xiaochong.loan.background.entity.po.RoleManage;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.RoleManageVo;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by wujiaxing on 2017/9/1.
 */
@Service("roleManageService")
public class RoleManageService extends BaseService {

	private Logger logger = LoggerFactory.getLogger(RoleManageService.class);

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	@Resource(name = "roleManageDao")
	private RoleManageDao roleManageDao;

	public BusinessVo<Boolean> addRole(String roleName, String status, String roleRemark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		RoleManage roleManage = new RoleManage();
		roleManage.setRoleName(roleName);
		roleManage.setStatus(status);
		roleManage.setRoleRemark(roleRemark);
		if(roleManageDao.insert(roleManage)==0){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("roleManageService.addRole新增失败！roleWebapp:{}",roleManage);
			return businessVo;
		}
		return businessVo;
	}

	public BusinessVo<BasePageInfoVo<RoleManageVo>> rolePage(Integer pageNum, Integer pageSize) {
		BusinessVo<BasePageInfoVo<RoleManageVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		PageHelper.startPage(pageNum, pageSize,true);
		Page<RoleManageVo> list =
				(Page<RoleManageVo>)roleManageDao.roleManagePage();

		//拼装成返回类
		PageInfo<RoleManageVo> orderListBoPageInfo = list.toPageInfo();
		//拼装分页类
		BasePageInfoVo<RoleManageVo> basePageInfoVo = assemblyBasePageInfo(orderListBoPageInfo);
		basePageInfoVo.setResultList(orderListBoPageInfo.getList());
		businessVo.setData(basePageInfoVo);
		return businessVo;
	}

    public BusinessVo<Boolean> updateRole(Integer roleId, String roleName, String status, String roleRemark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		RoleManage roleManage = roleManageDao.getById(roleId);
		if(roleManage==null){
			businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
			businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
			logger.warn("roleManageService.updateRole修改失败！roleId不存在！roleId:{}",roleId);
			return businessVo;
		}
		if(StringUtils.isNotBlank(roleName)){
			roleManage.setRoleName(roleName);
		}
		if(StringUtils.isNotBlank(status)){
			roleManage.setStatus(status);
		}
		if(StringUtils.isNotBlank(roleRemark)){
			roleManage.setRoleRemark(roleRemark);
		}

		if(roleManageDao.update(roleManage)!=1){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("roleManageService.updateRole修改失败！roleManage:{}",roleManage);
			return businessVo;
		}
		businessVo.setData(true);
		return businessVo;
    }

	public BusinessVo<List<RoleManageVo>> roleList() {
		BusinessVo<List<RoleManageVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		List<RoleManageVo> roleManageVos = roleManageDao.roleManagePage();

		businessVo.setData(roleManageVos);
		return businessVo;
	}
}
