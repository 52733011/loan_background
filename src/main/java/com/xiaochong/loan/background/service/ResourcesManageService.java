package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.ResourceMenuManageDao;
import com.xiaochong.loan.background.dao.ResourcesManageDao;
import com.xiaochong.loan.background.dao.RoleResourcesManageDao;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.po.ResourceMeunManage;
import com.xiaochong.loan.background.entity.po.ResourcesManage;
import com.xiaochong.loan.background.entity.po.RoleResourcesManage;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.ResourcesManageTypeEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by wujiaxing on 2017/9/1.
 *
 */
@Service("resourcesManageService")
public class ResourcesManageService extends BaseService {

	private Logger logger = LoggerFactory.getLogger(ResourcesManageService.class);

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	@Resource(name = "resourcesManageDao")
	private ResourcesManageDao resourcesManageDao;

	@Resource(name = "roleResourcesManageDao")
	private RoleResourcesManageDao roleResourcesManageDao;

	@Resource(name = "resourceMenuManageDao")
	private ResourceMenuManageDao resourceMenuManageDao;

	@Transactional
	public BusinessVo<Boolean> addResourcesManage(String name, String url, String status, Integer type, Integer parentId, String remark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ResourcesManage resources = new ResourcesManage();
		resources.setResName(name);
		resources.setResUrl(url);
		resources.setResRemark(remark);
		resources.setStatus(status);
		resources.setType(type);
		resources.setParentId(parentId);
		if(resourcesManageDao.insert(resources)!=1){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("resourcesManageService.addResourcesManage新增失败！resources:{}",resources);
			return businessVo;
		}
		resources.setSort(Integer.parseInt(resources.getId()+"00"));
		Date now = new Date();
		resources.setCreatetime(now);
		resources.setUpdatetime(now);
		resourcesManageDao.update(resources);
		businessVo.setData(true);
		return businessVo;
	}

	@Transactional
	public BusinessVo<Boolean> updateResourcesManage(Integer id, String name, String url, String status, Integer type, Integer parentId, String remark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ResourcesManage resources = resourcesManageDao.getById(id);
		resources.setResName(name);
		resources.setResUrl(url);
		resources.setResRemark(remark);
		resources.setStatus(status);
		resources.setType(type);
		resources.setParentId(parentId);
		if(resourcesManageDao.update(resources)!=1){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("resourcesManageService.addResourcesManage修改失败！resources:{}",resources);
			return businessVo;
		}

		businessVo.setData(true);
		return businessVo;
	}

    public BusinessVo<List<ResourcesManageVo>> listResourcesManage(Integer type) {
		BusinessVo<List<ResourcesManageVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		List<ResourcesManage> resourcesManages = resourcesManageDao.queryAllResources(type);
		List<ResourcesManageVo> resourcesVos = this.toResourcesVo(resourcesManages, null);
		businessVo.setData(resourcesVos);
		return businessVo;
    }

	public BusinessVo<List<ResourcesManageVo>> listResourcesManageUser(String token, Integer type) {
		BusinessVo<List<ResourcesManageVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ManageAdmin manageAdmin =
				sessionComponent.getAttributeManageAdmin(UserLoginTypeEnum.MANAGE.getType() + "-" + token);
		if(manageAdmin==null){
			businessVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
			businessVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
			return businessVo;
		}
		List<ResourcesManage> resourcesManages = resourcesManageDao.queryResources(manageAdmin.getId(),type);
		List<ResourcesManageVo> resourcesVos = this.toResourcesVo(resourcesManages, null);
		businessVo.setData(resourcesVos);
		return businessVo;
	}

	private List<ResourcesManageVo> toResourcesVo(List<ResourcesManage> resourcesManages,ResourcesManageVo resourcesManageVo) {
		if(resourcesManages == null){
			return null;
		}
		if(resourcesManageVo==null){
			resourcesManageVo = new ResourcesManageVo();
			resourcesManageVo.setId(0);
		}
		List<ResourcesManageVo> resourcesManageVos = new ArrayList<>();
		for (int i = 0; i < resourcesManages.size(); i++) {
			if(resourcesManageVo.getId().equals(resourcesManages.get(i).getParentId())){
				ResourcesManageVo vo = new ResourcesManageVo();
				vo.setResourcesWebapp(resourcesManages.get(i));
				resourcesManageVos.add(vo);
				resourcesManages.remove(i);
				i--;
			}
		}
		for (ResourcesManageVo returnVo: resourcesManageVos) {
			returnVo.setSonResources(this.toResourcesVo(resourcesManages, returnVo));
		}
		return resourcesManageVos;
	}

    public BusinessVo<List<RoleResourcesManageVo>> listRoleResourcesManage(Integer roleId,Integer type) {
		BusinessVo<List<RoleResourcesManageVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		List<RoleResourcesManageVo> roleResourcesVos =
				roleResourcesManageDao.listRoleResourcesManageVo(roleId,type);
		roleResourcesVos = this.toRoleResourcesVo(roleResourcesVos, null);
		businessVo.setData(roleResourcesVos);
		return businessVo;
    }

	private List<RoleResourcesManageVo> toRoleResourcesVo(List<RoleResourcesManageVo> roleResourcesVos,
														  RoleResourcesManageVo mainVo) {
		if(roleResourcesVos == null){
			return null;
		}
		if(mainVo==null){
			mainVo = new RoleResourcesManageVo();
			mainVo.setId(0);
		}
		List<RoleResourcesManageVo> returnVos = new ArrayList<>();
		for (int i = 0; i < roleResourcesVos.size(); i++) {
			if(mainVo.getId().equals(roleResourcesVos.get(i).getParentId())){
				returnVos.add(roleResourcesVos.get(i));
				roleResourcesVos.remove(i);
				i--;
			}
		}
		for (RoleResourcesManageVo returnVo: returnVos) {
			returnVo.setSonResources(this.toRoleResourcesVo(roleResourcesVos, returnVo));
		}
		return returnVos;
	}

	@Transactional(rollbackFor = Exception.class)
	public BusinessVo<Boolean> resourcesAssign(Integer roleId, String resourcesIds) throws DataDisposeException {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		RoleResourcesManage search = new RoleResourcesManage();
		search.setRoleId(roleId);
		List<RoleResourcesManage> roleResourcesManages =
				roleResourcesManageDao.listByRoleResourcesManage(search);
		if(roleResourcesManages!=null){
            for (RoleResourcesManage roleResourcesManage:roleResourcesManages) {
                if(roleResourcesManageDao.delete(roleResourcesManage.getId())!=1){
                    logger.error("删除角色权限失败！roleResourcesManage:{}",roleResourcesManage);
                    throw new DataDisposeException("删除角色权限失败！");
                }
            }
        }
		List<String> resourcesIdList = Arrays.asList(resourcesIds.split(","));
        for (String resourcesId: resourcesIdList) {
            RoleResourcesManage roleResourcesManage = new RoleResourcesManage();
            roleResourcesManage.setRoleId(roleId);
            roleResourcesManage.setResourcesId(Integer.parseInt(resourcesId));
            if(roleResourcesManageDao.insert(roleResourcesManage)!=1){
                logger.error("分配角色权限失败！roleResourcesManage:{}",roleResourcesManage);
                throw new DataDisposeException("分配角色权限失败！");
            }
			ResourcesManage resourcesManage = resourcesManageDao.getById(Integer.valueOf(resourcesId));
			//若子节点选中，父节点也必须选中
			if(resourcesManage!=null && resourcesManage.getParentId().intValue()!=0 &&
					!resourcesIdList.contains(resourcesManage.getParentId().toString())){
				roleResourcesManage = new RoleResourcesManage();
				roleResourcesManage.setRoleId(roleId);
				roleResourcesManage.setResourcesId(resourcesManage.getParentId());
				roleResourcesManageDao.insert(roleResourcesManage);
			}
        }
        businessVo.setData(true);
        return businessVo;

	}

    public BusinessVo<BasePageInfoVo<ResourcesManagePage>> resourcesManagePage(String resName, String resUrl, Integer parentId, Integer type, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<ResourcesManagePage>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		if(StringUtils.isBlank(resName)){
			resName = null;
		}
		if(StringUtils.isBlank(resUrl)){
			resUrl = null;
		}
        PageHelper.startPage(pageNum, pageSize,true);
        Page<ResourcesManagePage> list =
                (Page<ResourcesManagePage>)resourcesManageDao.resourcesManagePage(resName,resUrl,parentId,type);

        //拼装成返回类
        PageInfo<ResourcesManagePage> pageInfo = list.toPageInfo();
        //拼装分页类
        BasePageInfoVo<ResourcesManagePage> basePageInfoVo = assemblyBasePageInfo(pageInfo);
        basePageInfoVo.setResultList(pageInfo.getList());
        businessVo.setData(basePageInfoVo);
        return businessVo;
    }

	@Transactional(rollbackFor = Exception.class)
    public BusinessVo<Boolean> resourcesAssignMenu(Integer roleId, String menuIds) throws DataDisposeException {

		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		RoleResourcesManage search = new RoleResourcesManage();
		search.setRoleId(roleId);
		List<RoleResourcesManage> roleResourcesManages =
				roleResourcesManageDao.listByRoleResourcesManage(search);
		if(roleResourcesManages!=null){
			roleResourcesManages.forEach(roleResourcesManageDao::delete);
		}

		List<String> resourcesIdList = new ArrayList<>(Arrays.asList(menuIds.split(",")));
		resourcesIdList.add("0");
		for (String resourcesId:resourcesIdList) {
		    if(!"0".equals(resourcesId)){
                ResourcesManage resourcesManage = resourcesManageDao.getById(Integer.valueOf(resourcesId));
                if(!ResourcesManageTypeEnum.MENU.getType().equals(resourcesManage.getType())){
                    continue;
                }
                updateRoleResourcesWebapp(roleId,Integer.valueOf(resourcesId));
            }
			ResourceMeunManage searchResourceMeunManage = new ResourceMeunManage();
			searchResourceMeunManage.setMenuId(Integer.valueOf(resourcesId));
			List<ResourceMeunManage> resourceMeunManages =
					resourceMenuManageDao.listByResourceMeunManage(searchResourceMeunManage);
			if(resourceMeunManages!=null){
				for (ResourceMeunManage resourceMeunManage: resourceMeunManages) {
					updateRoleResourcesWebapp(roleId,resourceMeunManage.getResourcesId());
				}
			}


		}
		businessVo.setData(true);
		return businessVo;

	}

	private void updateRoleResourcesWebapp( Integer roleId, Integer resourcesId) throws DataDisposeException {
		RoleResourcesManage roleResourcesManage = new RoleResourcesManage();
		roleResourcesManage.setResourcesId(resourcesId);
		roleResourcesManage.setRoleId(roleId);
		if(roleResourcesManageDao.insert(roleResourcesManage)!=1){
			logger.error("分配权限失败！roleResourcesManage:{}",roleResourcesManage);
			throw new DataDisposeException("分配权限失败！");
		}

	}

	public BusinessVo<Boolean> resourcesMenuManage(Integer menuId, String resourcesIds) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ResourceMeunManage search = new ResourceMeunManage();
		search.setMenuId(menuId);
		List<ResourceMeunManage> resourceMeunManages = resourceMenuManageDao.listByResourceMeunManage(search);
		if(resourceMeunManages!=null){
			resourceMeunManages.forEach(resourceMenuManageDao::delete);
		}
		if(StringUtils.isNotBlank(resourcesIds)){
			Arrays.stream(resourcesIds.split(",")).forEach(r->{
				ResourceMeunManage resourceMeunWebapp = new ResourceMeunManage();
				resourceMeunWebapp.setMenuId(menuId);
				resourceMeunWebapp.setResourcesId(Integer.valueOf(r));
				resourceMenuManageDao.insert(resourceMeunWebapp);
			});
			businessVo.setData(true);
		}
		return businessVo;
	}
}
