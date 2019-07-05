package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
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
 * 前端企业权限接口
 */
@Service("resourcesWebappService")
public class ResourcesWebappService extends BaseService{

	private Logger logger = LoggerFactory.getLogger(ResourcesWebappService.class);

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	@Resource(name = "resourcesWebappDao")
	private ResourcesWebappDao resourcesWebappDao;

	@Resource(name = "roleResourcesWebappDao")
	private RoleResourcesWebappDao roleResourcesWebappDao;

	@Resource(name = "proxyUserDao")
	private ProxyUserDao proxyUserDao;

	@Resource(name = "userRoleWebappDao")
	private UserRoleWebappDao userRoleWebappDao;

	@Resource(name = "resourceMenuWebappDao")
	private ResourceMenuWebappDao resourceMenuWebappDao;

	@Resource(name = "merchantDao")
	private MerchantDao merchantDao;

	@Resource(name = "roleWebappDao")
	private RoleWebappDao roleWebappDao;

	@Resource(name = "smsTemplateDao")
	private SmsTemplateDao smsTemplateDao;

	@Resource(name = "merchResourcesWebappDao")
	private MerchResourcesWebappDao merchResourcesWebappDao;


	public BusinessVo<List<MenuWebappVo>> queryMenu(String token) {
		BusinessVo<List<MenuWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		if(StringUtils.isBlank(proxyUserId)){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("ResourcesWebappService.queryMenu登录失效token:{}！",token);
			return businessVo;
		}
		businessVo.setData(this.queryMenuIn(proxyUserId, ResourcesWebappTypeEnum.MENU));
		return businessVo;
	}

	public BusinessVo<List<MenuWebappVo>> queryTool(String token) {
		BusinessVo<List<MenuWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		if(StringUtils.isBlank(proxyUserId)){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("ResourcesWebappService.queryTool登录失效token:{}！",token);
			return businessVo;
		}
		businessVo.setData(this.queryMenuIn(proxyUserId,ResourcesWebappTypeEnum.TOOL));
		return businessVo;
	}

	public BusinessVo<List<MenuWebappVo>> queryButton(String token) {
		BusinessVo<List<MenuWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		if(StringUtils.isBlank(proxyUserId)){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("ResourcesWebappService.queryButton:{}！",token);
			return businessVo;
		}
		businessVo.setData(this.queryMenuIn(proxyUserId,ResourcesWebappTypeEnum.BUTTON));
		return businessVo;
	}

	public BusinessVo<List<MenuWebappVo>> queryMenuByRoleId(String roleId) {
		BusinessVo<List<MenuWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		List<MenuWebappVo> resourcesWebapps =
				resourcesWebappDao.queryResourcesRoleSelect(roleId, ResourcesWebappTypeEnum.MENU.getType());
		List<MenuWebappVo> menuWebappVos = this.toMenuVo(resourcesWebapps, null);
		businessVo.setData(menuWebappVos);
		return businessVo;
	}

	public List<MenuWebappVo> queryMenuIn(String proxyUserId,ResourcesWebappTypeEnum resourcesWebappTypeEnum) {
		ProxyUser proxyUser = proxyUserDao.getById(Integer.valueOf(proxyUserId));
		List<MenuWebappVo> menuWebappVos;
		if(IsTypeEnum.TRUE.getType().equals(proxyUser.getIsMaster())){
			menuWebappVos =
					resourcesWebappDao.queryResourcesMerchSelect(proxyUser.getMerchId(), resourcesWebappTypeEnum.getType());
		}else {
			menuWebappVos =
					resourcesWebappDao.queryResourcesUserSelect(proxyUserId, resourcesWebappTypeEnum.getType());
		}
		return this.toMenuVo(menuWebappVos,null);

	}


	private List<MenuWebappVo> toMenuVo(List<MenuWebappVo> resourcesWebapps,MenuWebappVo menuWebappVo) {
		if(resourcesWebapps == null){
			return null;
		}
		if(menuWebappVo==null){
			menuWebappVo = new MenuWebappVo();
			menuWebappVo.setId(0);
		}
		List<MenuWebappVo> menuWebappVos = new ArrayList<>();
		for (int i = 0; i < resourcesWebapps.size(); i++) {
			if(menuWebappVo.getId().equals(resourcesWebapps.get(i).getParentId())){
				MenuWebappVo vo = resourcesWebapps.get(i);
				menuWebappVos.add(vo);
				resourcesWebapps.remove(i);
				i--;
			}
		}
		for (MenuWebappVo returnMenu: menuWebappVos) {
			returnMenu.setSonMenu(this.toMenuVo(resourcesWebapps, returnMenu));
		}
		return menuWebappVos;
	}


	@Transactional(rollbackFor = Exception.class)
	public BusinessVo<Boolean> resourcesAssign(String token, Integer roleId, String resourcesIds) throws DataDisposeException {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("resourcesWebappService.resourcesAssign！token:{}",token);
			return businessVo;
		}
		RoleResourcesWebapp search = new RoleResourcesWebapp();
		search.setMerchId(proxyUser.getMerchId());
		search.setRoleId(roleId);
		List<RoleResourcesWebapp> roleResourcesWebapps =
				roleResourcesWebappDao.listByRoleResourcesWebapp(search);
		if(roleResourcesWebapps!=null){
			for (RoleResourcesWebapp roleResourcesWebapp:roleResourcesWebapps) {
				if(roleResourcesWebappDao.delete(roleResourcesWebapp.getId())!=1){
					logger.error("删除权限失败！roleResourcesWebapp:{}",roleResourcesWebapp);
					throw new DataDisposeException("删除权限失败！");
				}
			}
		}
		List<String> resourcesIdList = Arrays.asList(resourcesIds.split(","));
		for (String resourcesId:resourcesIdList) {
			updateRoleResourcesWebapp(proxyUser.getMerchId(),roleId,Integer.valueOf(resourcesId));
			ResourcesWebapp resourcesWebapp = resourcesWebappDao.getById(Integer.valueOf(resourcesId));
			//若子节点选中，父节点也必须选中
			if(resourcesWebapp!=null && resourcesWebapp.getParentId().intValue()!=0 &&
					!resourcesIdList.contains(resourcesWebapp.getParentId().toString())){
				RoleResourcesWebapp roleResourcesWebapp = new RoleResourcesWebapp();
				roleResourcesWebapp.setMerchId(proxyUser.getMerchId());
				roleResourcesWebapp.setResourcesId(resourcesWebapp.getParentId());
				roleResourcesWebapp.setRoleId(roleId);
				roleResourcesWebappDao.insert(roleResourcesWebapp);
			}
		}
		businessVo.setData(true);
		return businessVo;
	}


	private void updateRoleResourcesWebapp(Integer merchId, Integer roleId, Integer resourcesId) throws DataDisposeException {
		RoleResourcesWebapp roleResourcesWebapp = new RoleResourcesWebapp();
		roleResourcesWebapp.setMerchId(merchId);
		roleResourcesWebapp.setResourcesId(resourcesId);
		roleResourcesWebapp.setRoleId(roleId);
		if(roleResourcesWebappDao.insert(roleResourcesWebapp)!=1){
			logger.error("分配权限失败！roleResourcesWebapp:{}",roleResourcesWebapp);
			throw new DataDisposeException("分配权限失败！");
		}

	}

	@Transactional
	public BusinessVo<Boolean> addResourcesWebapp(String name, String url, String status,
												  Integer type, Integer parentId, String remark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ResourcesWebapp resources = new ResourcesWebapp();
		resources.setResName(name);
		resources.setResUrl(url);
		resources.setResRemark(remark);
		resources.setStatus(status);
		resources.setType(type);
		resources.setParentId(parentId);
		if(resourcesWebappDao.insert(resources)==0){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("resourcesManageService.addResourcesWebapp新增失败！resources:{}",resources);
			return businessVo;
		}
		resources.setSort(Integer.parseInt(resources.getId()+"00"));
		Date now = new Date();
		resources.setCreatetime(now);
		resources.setUpdatetime(now);
		resourcesWebappDao.update(resources);
		businessVo.setData(true);
		return businessVo;
	}

	public BusinessVo<Boolean> updateResourcesWebapp(Integer id, String name, String url, String status, Integer type, Integer parentId, String remark) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ResourcesWebapp resources = resourcesWebappDao.getById(id);
		if(StringUtils.isNotBlank(name)){
			resources.setResName(name);
		}
		if(StringUtils.isNotBlank(url)){
			resources.setResUrl(url);
		}
		if(StringUtils.isNotBlank(status)){
			resources.setStatus(status);
		}
		if(type!=null){
			resources.setType(type);
		}
		if(parentId!=null){
			resources.setParentId(parentId);
		}
		if(StringUtils.isNotBlank(remark)){
			resources.setResRemark(remark);
		}

		resources.setSort(Integer.parseInt(resources.getId()+"00"));
		if(resourcesWebappDao.update(resources)==0){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("resourcesManageService.updateResourcesWebapp更新失败！resources:{}",resources);
			return businessVo;
		}
		businessVo.setData(true);
		return businessVo;
	}

	public BusinessVo<List<ResourcesWebappVo>> listResourcesWebapp(Integer merchId,String types) {
		BusinessVo<List<ResourcesWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		List<ResourcesWebappVo> roleResourcesWebapps = new ArrayList<>();
		if(StringUtils.isBlank(types)){
			roleResourcesWebapps= resourcesWebappDao.listResourcesWebapp(merchId,null);
		}else{
			String[] split = types.split(",");
			for (String s: split) {
				List<ResourcesWebappVo> voList = resourcesWebappDao.listResourcesWebapp(merchId, Integer.valueOf(s));
				if(CollectionUtil.isNotBlank(voList)){
					roleResourcesWebapps.addAll(voList);
				}
			}
		}

		roleResourcesWebapps = this.toResourcesVo(roleResourcesWebapps, null);
		businessVo.setData(roleResourcesWebapps);
		return businessVo;
	}

	private List<ResourcesWebappVo> toResourcesVo(List<ResourcesWebappVo> resourcesWebapps,ResourcesWebappVo main) {
		if(resourcesWebapps == null){
			return null;
		}
		if(main==null){
			main = new ResourcesWebappVo();
			main.setId(0);
		}
		List<ResourcesWebappVo> retuenVos = new ArrayList<>();
		for (int i = 0; i < resourcesWebapps.size(); i++) {
			if(main.getId().equals(resourcesWebapps.get(i).getParentId())){
				retuenVos.add(resourcesWebapps.get(i));
				resourcesWebapps.remove(i);
				i--;
			}
		}
		for (ResourcesWebappVo returnVo: retuenVos) {
			returnVo.setSonResources(this.toResourcesVo(resourcesWebapps, returnVo));
		}
		return retuenVos;
	}

	public BusinessVo<BasePageInfoVo<ResourcesWebappPage>> resourcesWebappPage(Integer parentId, Integer type, String resName, String resUrl, Integer pageNum, Integer pageSize) {
		BusinessVo<BasePageInfoVo<ResourcesWebappPage>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		if(StringUtils.isBlank(resName)){
			resName = null;
		}
		if(StringUtils.isBlank(resUrl)){
			resUrl = null;
		}
		PageHelper.startPage(pageNum, pageSize,true);
		Page<ResourcesWebappPage> list =
				(Page<ResourcesWebappPage>)resourcesWebappDao.resourcesWebappPage(parentId,type,resName,resUrl);

		//拼装成返回类
		PageInfo<ResourcesWebappPage> pageInfo = list.toPageInfo();
		//拼装分页类
		BasePageInfoVo<ResourcesWebappPage> basePageInfoVo = assemblyBasePageInfo(pageInfo);
		basePageInfoVo.setResultList(pageInfo.getList());
		businessVo.setData(basePageInfoVo);
		return businessVo;
	}

    public BusinessVo<List<MenuWebappVo>> queryMerchMenu(String token,Integer roleId,String types) {
		BusinessVo<List<MenuWebappVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("resourcesWebappService.queryMerchMenu！token:{}",token);
			return businessVo;
		}
		List<MenuWebappVo> menuWebappVos = new ArrayList<>();
		if(StringUtils.isBlank(types)){
			menuWebappVos = resourcesWebappDao.queryMerchMenu(proxyUser.getMerchId(),roleId,null);
		}else{
			String[] split = types.split(",");
			for (String s: split) {
				List<MenuWebappVo> mvo =
						resourcesWebappDao.queryMerchMenu(proxyUser.getMerchId(), roleId, Integer.valueOf(s));
				if(CollectionUtil.isNotBlank(mvo)){
					menuWebappVos.addAll(mvo);
				}
			}
		}

		menuWebappVos = this.toMenuWebappVo(menuWebappVos, null);
		businessVo.setData(menuWebappVos);
		return businessVo;
    }

	private List<MenuWebappVo> toMenuWebappVo(List<MenuWebappVo> resourcesWebapps,MenuWebappVo main) {
		if(resourcesWebapps == null){
			return null;
		}
		if(main==null){
			main = new MenuWebappVo();
			main.setId(0);
		}
		List<MenuWebappVo> retuenVos = new ArrayList<>();
		for (int i = 0; i < resourcesWebapps.size(); i++) {
			if(main.getId().equals(resourcesWebapps.get(i).getParentId())){
				retuenVos.add(resourcesWebapps.get(i));
				resourcesWebapps.remove(i);
				i--;
			}
		}
		for (MenuWebappVo returnVo: retuenVos) {
			returnVo.setSonMenu(this.toMenuWebappVo(resourcesWebapps, returnVo));
		}
		return retuenVos;
	}


	public BusinessVo<Boolean> resourcesMenuWebapp(Integer menuId, String resourcesIds) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		ResourceMeunWebapp search = new ResourceMeunWebapp();
		search.setMenuId(menuId);
		List<ResourceMeunWebapp> resourceMeunWebapps = resourceMenuWebappDao.listByResourceMeunWebapp(search);
		if(resourceMeunWebapps!=null){
			resourceMeunWebapps.forEach(resourceMenuWebappDao::delete);
		}
		if(StringUtils.isNotBlank(resourcesIds)){
			Arrays.stream(resourcesIds.split(",")).forEach(r->{
				ResourceMeunWebapp resourceMeunWebapp = new ResourceMeunWebapp();
				resourceMeunWebapp.setMenuId(menuId);
				resourceMeunWebapp.setResourcesId(Integer.valueOf(r));
				resourceMenuWebappDao.insert(resourceMeunWebapp);
			});
			businessVo.setData(true);
		}
		return businessVo;
	}


	@Transactional(rollbackFor = Exception.class)
	public BusinessVo<Boolean> resourcesAssignMenu(String token, Integer roleId, String menuIds) throws DataDisposeException {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("resourcesWebappService.resourcesAssignMenu！token:{}",token);
			return businessVo;
		}
		RoleResourcesWebapp search = new RoleResourcesWebapp();
		search.setMerchId(proxyUser.getMerchId());
		search.setRoleId(roleId);
		List<RoleResourcesWebapp> roleResourcesWebapps =
				roleResourcesWebappDao.listByRoleResourcesWebapp(search);
		if(roleResourcesWebapps!=null){
			roleResourcesWebapps.forEach(roleResourcesWebappDao::delete);
		}

		List<String> resourcesIdList = new ArrayList<>(Arrays.asList(menuIds.split(",")));
		resourcesIdList.add("0");
		for (String resourcesId:resourcesIdList) {
			if(!"0".equals(resourcesId)){
				ResourcesWebapp resourcesWebapp = resourcesWebappDao.getById(Integer.valueOf(resourcesId));
				if(!ResourcesWebappTypeEnum.MENU.getType().equals(resourcesWebapp.getType()) &&
						!ResourcesWebappTypeEnum.TOOL.getType().equals(resourcesWebapp.getType())){
					continue;
				}
				updateRoleResourcesWebapp(proxyUser.getMerchId(),roleId,Integer.valueOf(resourcesId));
			}
			ResourceMeunWebapp searchResourceMeunWebapp = new ResourceMeunWebapp();
			searchResourceMeunWebapp.setMenuId(Integer.valueOf(resourcesId));
			List<ResourceMeunWebapp> resourceMeunWebapps =
					resourceMenuWebappDao.listByResourceMeunWebapp(searchResourceMeunWebapp);
			if(resourceMeunWebapps!=null){
				for (ResourceMeunWebapp resourceMeunWebapp: resourceMeunWebapps) {
					updateRoleResourcesWebapp(proxyUser.getMerchId(),roleId,resourceMeunWebapp.getResourcesId());
				}
			}


		}
		businessVo.setData(true);
		return businessVo;
	}


	@Transactional
	public void resourcesInitialization(){
		List<Merchantinfo> merchantinfos = merchantDao.selectMerchantinfo(new Merchantinfo());
		List<ResourcesWebapp> resourcesWebapps = resourcesWebappDao.listByResourcesWebapp(new ResourcesWebapp());
		for (Merchantinfo merchantinfo:merchantinfos) {
			logger.info("resourcesInitialization,merchantinfo = ");

			if(StringUtils.isBlank(merchantinfo.getAuditType())){
				merchantinfo.setAuditType(LoanAuditTypeEnum.MERCHANT_AUDIT.getType());
				merchantDao.updateMerchant(merchantinfo);
			}
			RoleWebapp roleWebapp = new RoleWebapp();
			roleWebapp.setRoleName("初始角色");
			roleWebapp.setRoleRemark("系统生成默认角色");
			roleWebapp.setMerchId(merchantinfo.getId());
			if(roleWebappDao.insert(roleWebapp)!=1){
				continue;
			}
			for (ResourcesWebapp resourcesWebapp:resourcesWebapps) {
				MerchResourcesWebapp merchResourcesWebapp = new MerchResourcesWebapp();
				merchResourcesWebapp.setMerchId(merchantinfo.getId());
				merchResourcesWebapp.setResourcesId(resourcesWebapp.getId());
				merchResourcesWebappDao.insert(merchResourcesWebapp);
				if("变更子账户状态".equals(resourcesWebapp.getResName())||
					"添加子账户".equals(resourcesWebapp.getResName())||
					"新增角色".equals(resourcesWebapp.getResName())||
					"修改角色".equals(resourcesWebapp.getResName())||
					"下属账户".equals(resourcesWebapp.getResName())||
					"修改子账户角色".equals(resourcesWebapp.getResName())||
					"权限分配".equals(resourcesWebapp.getResName())||
					"菜单权限分配".equals(resourcesWebapp.getResName())||
					"新增下属账户".equals(resourcesWebapp.getResName())||
					"新增下属账户".equals(resourcesWebapp.getResName())||
					ResourcesWebappTypeEnum.TOOL.getType().equals(resourcesWebapp.getType())
						){
						continue;
				}
				RoleResourcesWebapp roleResourcesWebapp = new RoleResourcesWebapp();
				roleResourcesWebapp.setRoleId(roleWebapp.getId());
				roleResourcesWebapp.setMerchId(merchantinfo.getId());
				roleResourcesWebapp.setResourcesId(resourcesWebapp.getId());
				roleResourcesWebappDao.insert(roleResourcesWebapp);


			}
			ProxyUser searchUser = new ProxyUser();
			searchUser.setStatus(null);
			searchUser.setMerchId(merchantinfo.getId());
			List<ProxyUser> proxyUsers = proxyUserDao.listByProxyUser(searchUser);
			if(CollectionUtil.isBlank(proxyUsers)){
				continue;
			}
			for (ProxyUser proxyUser:proxyUsers) {
				UserRoleWebapp userRoleWebapp = new UserRoleWebapp();
				userRoleWebapp.setUserId(proxyUser.getId());
				userRoleWebapp = userRoleWebappDao.getByUserRoleWebapp(userRoleWebapp);
				if(userRoleWebapp==null){
					userRoleWebapp = new UserRoleWebapp();
					userRoleWebapp.setMerchId(merchantinfo.getId());
					userRoleWebapp.setUserId(proxyUser.getId());
					userRoleWebapp.setRoleId(roleWebapp.getId());
					userRoleWebappDao.insert(userRoleWebapp);
				}
			}



			//生成短信模板
			for (SmsSendStatueEnum smsSendStatueEnum:SmsSendStatueEnum.values()) {
				SmsTemplate smsTemplate = new SmsTemplate();
				smsTemplate.setMerchId(merchantinfo.getId());

				smsTemplate.setSendStatus(smsSendStatueEnum.getType());
				smsTemplate = smsTemplateDao.getBySmsTemplate(smsTemplate);
				if(smsTemplate==null){
					smsTemplate = new SmsTemplate();
					smsTemplate.setMerchId(merchantinfo.getId());
					smsTemplate.setSendStatus(smsSendStatueEnum.getType());
					smsTemplate.setStatus(IsTypeEnum.FALSE.getType());
					smsTemplate.setUpdateUser(0);
					String company_name = "/%"+ SmaTagEnum.COMPANY_NAME.getName()+"%/";
					String borrowing_information = "/%"+ SmaTagEnum.BORROWING_INFORMATION.getName()+"%/";
					String bank_card_no = "/%"+ SmaTagEnum.BANK_CARD_NO.getName()+"%/";
					String next_repayment = "/%"+ SmaTagEnum.NEXT_REPAYMENT.getName()+"%/";
					String repayment = "/%"+ SmaTagEnum.REPAYMENT.getName()+"%/";
					String billing_information = "/%"+ SmaTagEnum.BILLING_INFORMATION.getName()+"%/";
					String overdue_days = "/%"+ SmaTagEnum.OVERDUE_DAYS.getName()+"%/";
					if(SmsSendStatueEnum.STAGED_LOAN_SUCCESS.getType().equals(smsSendStatueEnum.getType())){
						String content = company_name+"你的"+borrowing_information+
								"已审核通过，款项已经打入尾号为"+bank_card_no+
								"的银行卡，资金1-3个小时到账，如有疑问请联系1890000000，"+
								repayment+"，查看账单"+billing_information+"。";
						smsTemplate.setContent(content);
					}else if(SmsSendStatueEnum.REPAYMENT_SUCCESS.getType().equals(smsSendStatueEnum.getType())){
						String content = company_name+"你的"+borrowing_information+
								"，"+next_repayment+
								"，"+"，"+billing_information+"。";
						smsTemplate.setContent(content);
					}else if(SmsSendStatueEnum.LAST_REPAYMENT_SUCCESS.getType().equals(smsSendStatueEnum.getType())){
						String content = company_name+"你的"+borrowing_information+
								"，"+repayment+"。";
						smsTemplate.setContent(content);
					}else if(SmsSendStatueEnum.NEAR_REPAYMENT.getType().equals(smsSendStatueEnum.getType())){
						String content = company_name+"你的"+borrowing_information+
								"，"+repayment+",逾期将产生高额费用，"+billing_information+"。";
						smsTemplate.setContent(content);
					}else if(SmsSendStatueEnum.OVERDUE.getType().equals(smsSendStatueEnum.getType())){
						String content = company_name+"你的"+borrowing_information+
								"，"+overdue_days+"，"+billing_information+"。";
						smsTemplate.setContent(content);
					}
					smsTemplateDao.insert(smsTemplate);
				}
			}
		}



	}



}
