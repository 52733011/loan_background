package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.RoleWebappVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.ResourcesWebappTypeEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by wujiaxing on 2017/9/1.
 */
@Service("roleWebappService")
public class RoleWebappService extends BaseService {

	private Logger logger = LoggerFactory.getLogger(RoleWebappService.class);

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	@Resource(name = "roleWebappDao")
	private RoleWebappDao roleWebappDao;

	@Resource(name = "proxyUserDao")
	private ProxyUserDao proxyUserDao;

	@Resource(name = "resourcesWebappDao")
	private ResourcesWebappDao resourcesWebappDao;

	@Resource(name = "roleResourcesWebappDao")
	private RoleResourcesWebappDao roleResourcesWebappDao;

	@Resource(name = "resourceMenuWebappDao")
	private ResourceMenuWebappDao resourceMenuWebappDao;

	public BusinessVo<BasePageInfoVo<RoleWebappVo>> rolePage(String token, Integer pageNum, Integer pageSize) {

		BusinessVo<BasePageInfoVo<RoleWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("RoleWebappService.rolePage登录失效！token:{}",token);
			return businessVo;
		}
		PageHelper.startPage(pageNum, pageSize,true);
		Page<RoleWebappVo> list =
				(Page<RoleWebappVo>)roleWebappDao.roleWebappPage(proxyUserId);

		//拼装成返回类
		PageInfo<RoleWebappVo> orderListBoPageInfo = list.toPageInfo();
		//拼装分页类
		BasePageInfoVo<RoleWebappVo> basePageInfoVo = assemblyBasePageInfo(orderListBoPageInfo);
		basePageInfoVo.setResultList(orderListBoPageInfo.getList());
		businessVo.setData(basePageInfoVo);
		return businessVo;
	}

	@Transactional
	public BusinessVo<Boolean> addRole(String token, String roleName, String status, String roleRemark) throws DataDisposeException {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("RoleWebappService.addRole登录失效！token:{}",token);
			return businessVo;
		}
		RoleWebapp roleWebapp = new RoleWebapp();
		roleWebapp.setMerchId(proxyUser.getMerchId());
		roleWebapp.setRoleName(roleName);
		roleWebapp.setStatus(status);
		roleWebapp.setRoleRemark(roleRemark);
		if(roleWebappDao.insert(roleWebapp)==0){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("RoleWebappService.addRole新增失败！roleWebapp:{}",roleWebapp);
			return businessVo;
		}
		List<Integer> resourcesIdList = new ArrayList<>(Arrays.asList(new Integer[]{0,63,64,65,66,67,68,69,70,71}));
		for (Integer resourcesId:resourcesIdList) {
			if(!new Integer(0).equals(resourcesId)){
				ResourcesWebapp resourcesWebapp = resourcesWebappDao.getById(resourcesId);
				if(!ResourcesWebappTypeEnum.MENU.getType().equals(resourcesWebapp.getType()) &&
						!ResourcesWebappTypeEnum.TOOL.getType().equals(resourcesWebapp.getType())){
					continue;
				}
				updateRoleResourcesWebapp(proxyUser.getMerchId(),roleWebapp.getId(),resourcesId);
			}
			ResourceMeunWebapp searchResourceMeunWebapp = new ResourceMeunWebapp();
			searchResourceMeunWebapp.setMenuId(resourcesId);
			List<ResourceMeunWebapp> resourceMeunWebapps =
					resourceMenuWebappDao.listByResourceMeunWebapp(searchResourceMeunWebapp);
			if(resourceMeunWebapps!=null){
				for (ResourceMeunWebapp resourceMeunWebapp: resourceMeunWebapps) {
					updateRoleResourcesWebapp(proxyUser.getMerchId(),roleWebapp.getId(),resourceMeunWebapp.getResourcesId());
				}
			}


		}
		return businessVo;
	}

	private void updateRoleResourcesWebapp(Integer merchId, Integer roleId, Integer resourcesId) throws DataDisposeException {
		RoleResourcesWebapp roleResourcesWebapp = new RoleResourcesWebapp();
		roleResourcesWebapp.setMerchId(merchId);
		roleResourcesWebapp.setResourcesId(resourcesId);
		roleResourcesWebapp.setRoleId(roleId);
		if(roleResourcesWebappDao.insert(roleResourcesWebapp)!=1){
			logger.error("addRole分配权限失败！roleResourcesWebapp:{}",roleResourcesWebapp);
			throw new DataDisposeException("分配权限失败！");
		}

	}

    public BusinessVo<List<RoleWebappVo>> roleList(String token) {
		BusinessVo<List<RoleWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("RoleWebappService.roleList登录失效！token:{}",token);
			return businessVo;
		}
		List<RoleWebappVo> roleWebappVos = roleWebappDao.roleWebappPage(proxyUserId);

		businessVo.setData(roleWebappVos);
		return businessVo;
    }

	public BusinessVo<Boolean> updateRole(String token, Integer roleId, String roleName, String status, String roleRemark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("RoleWebappService.updateRole登录失效！token:{}",token);
			return businessVo;
		}
		RoleWebapp roleWebapp = roleWebappDao.getById(roleId);
		if(roleWebapp==null){
			businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
			businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
			logger.warn("RoleWebappService.updateRole角色获取失败！roleId:{}",roleId);
			return businessVo;
		}
		if(StringUtils.isNotBlank(roleName)){
			roleWebapp.setRoleName(roleName);
		}
		if(StringUtils.isNotBlank(status)){
			roleWebapp.setStatus(status);
		}
		if(StringUtils.isNotBlank(roleRemark)){
			roleWebapp.setRoleRemark(roleRemark);
		}
		if(roleWebappDao.update(roleWebapp)==0){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("RoleWebappService.addRole修改失败！roleWebapp:{}",roleWebapp);
			return businessVo;
		}
		return businessVo;

	}
}
