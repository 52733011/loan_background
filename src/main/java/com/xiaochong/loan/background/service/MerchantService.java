package com.xiaochong.loan.background.service;

import com.alibaba.fastjson.util.TypeUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.AppInit;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.component.SMSComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.MerchantNameVo;
import com.xiaochong.loan.background.entity.vo.MerchantinfoVo;
import com.xiaochong.loan.background.exception.FileException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service("merchantService")
public class MerchantService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(MerchantService.class);

    @Resource(name = "merchantDao")
    private MerchantDao merchantDao;

    @Resource(name = "proxyUserDao")
	private ProxyUserDao proxyUserDao;

    @Resource(name = "smsComponent")
    private SMSComponent smsComponent;

    @Resource(name = "component.OssComponent")
    private OssComponent ossComponent;

    @Autowired
    private SessionComponent sessionComponent;

    @Resource(name = "proxyUserService")
    private ProxyUserService proxyUserService;

    @Resource(name = "roleResourcesWebappDao")
    private RoleResourcesWebappDao roleResourcesWebappDao;

    @Resource(name = "resourcesWebappDao")
    private ResourcesWebappDao resourcesWebappDao;

    @Autowired
    private MerchantAccountDao merchantAccountDao;

    @Autowired
    private FlowOptionDao flowOptionDao;

    @Autowired
    private MerchantFlowOptionDao merchantFlowOptionDao;

    @Autowired
    private MerchantinfoFlowDao merchantinfoFlowDao;

    @Resource(name = "merchResourcesWebappDao")
    private MerchResourcesWebappDao merchResourcesWebappDao;

    @Resource(name = "smsTemplateDao")
    private SmsTemplateDao smsTemplateDao;

    @Resource(name = "resourceMenuWebappDao")
    private ResourceMenuWebappDao resourceMenuWebappDao;

    @Value("${back.loanMerchUrl}")
    String loanMerchUrl;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public BusinessVo<MerchantinfoVo> findMerchantinfoByToid(Integer id) {
        BusinessVo<MerchantinfoVo> businessVo = new BusinessVo<>();
        Merchantinfo merchantinfoByToid = merchantDao.findMerchantinfoByToid(id);
        MerchantinfoVo merchantinfoVo = new MerchantinfoVo();
        merchantinfoConvert(merchantinfoVo,merchantinfoByToid);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_DESC);
        businessVo.setData(merchantinfoVo);
        return businessVo;
    }

    /**
     * 商户添加
     * @param token token
     * @param merchantName merchantName
     * @param shortName shortName
     * @param enName enName
     * @param linkMan linkMan
     * @param linkMobile linkMobile
     * @param linkEmail linkEmail
     * @param address address
     * @param scale scale
     * @param industry industry
     * @param cooperantBeginTime cooperantBeginTime
     * @param cooperantStopTime cooperantStopTime
     * @param pwd pwd
     * @param certificate certificate
     * @param agreement agreement
     * @param menuIds menuIds
     * @return String
     */
	@Transactional
	public BusinessVo<String> insertMerchant(String token,
                                             String merchantName,
                                             String shortName,
                                             String enName,
                                             String linkMan,
                                             String linkMobile,
                                             String linkEmail,
                                             String address,
                                             String scale,
                                             String industry,
                                             String cooperantBeginTime,
                                             String cooperantStopTime,
                                             String pwd,
                                             String auditType,
                                             MultipartFile certificate,
                                             MultipartFile agreement,
                                             String menuIds){
        BusinessVo<String> businessVo = new BusinessVo<>();
        //公司名称
        Merchantinfo merchantinfoForSelect= new Merchantinfo();
        merchantinfoForSelect.setMerchantName(merchantName);
        List<Merchantinfo> merchantinfoList1 = merchantDao.selectMerchantinfo(merchantinfoForSelect);
        if(CollectionUtils.isNotEmpty(merchantinfoList1)){
            businessVo.setData("该商户名称已经存在！");
            businessVo.setCode(BusinessConstantsUtils.MERCHANT_NAME_EXIST_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.MERCHANT_NAME_EXIST_ERROR_DESC);
            return businessVo;
        }
        ProxyUser proxyUserForSelect= new ProxyUser();
        proxyUserForSelect.setPhone(linkMobile);
        ProxyUser byProxyUser = proxyUserDao.getByProxyUser(proxyUserForSelect);
        if(byProxyUser!=null){
            businessVo.setData("该商户联系人已经存在！");
            businessVo.setCode(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ACCOUNT_EXIST_ERROR_DESC);
            return businessVo;
        }
        Merchantinfo info = new Merchantinfo();
        info.setMerchantName(merchantName);
        info.setLinkMan(linkMan);
        info.setLinkMobile(linkMobile);
        info.setShortName(shortName);
        info.setEnName(enName);
        info.setAddress(address);
        info.setIndustry(industry);
        info.setScale(scale);
        info.setAuditType(auditType);
        info.setLinkEmail(linkEmail);
        info.setCooperantBeginTime(DateUtils.stringToDate(cooperantBeginTime,DateUtils.yyyyMMdd_format));
        info.setCooperantStopTime(DateUtils.stringToDate(cooperantStopTime,DateUtils.yyyyMMdd_format));
        StringBuilder key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
        ManageAdmin manager = sessionComponent.getAttributeManageAdmin(key.toString());
        info.setCreateBy(manager.getId());
        info.setUpdateBy(manager.getId());
        String agreementNameUrl=null;
        String certificateUrl=null;
        String certificateName = null;
        String agreementNameName = null;
        if(null != certificate && !certificate.isEmpty()) {
            certificateName = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8)+certificate.getOriginalFilename();
            certificateUrl= uploadFile(certificate,certificateName);
        }
        info.setCertificateName(certificateName);
        info.setCertificateUrl(certificateUrl);
        if(null != agreement && !agreement.isEmpty()) {
            agreementNameName = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8)+agreement.getOriginalFilename();
            agreementNameUrl= uploadFile(agreement,agreementNameName);
        }
        info.setAgreementName(agreementNameName);
        info.setAgreementUrl(agreementNameUrl);
        info.setStatus(MerchStatusEnum.USING.getType());
        info.setCreatetime(new Date());
        info.setUpdatetime(new Date());



		ProxyUser proxyUser = new ProxyUser();
        proxyUser.setUsername(linkMan);
        proxyUser.setPwd(new MD5(pwd).compute());
        proxyUser.setPhone(linkMobile);
        proxyUser.setEmail(linkEmail);
        proxyUser.setStatus(ProxyUserStatusEnum.EFFECTIVE.getType());
        proxyUser.setFirstLogin(ProxyUserFirstLoginEnum.YES.getType());
        proxyUser.setCreatetime(info.getCreatetime());
        proxyUser.setIsMaster(ProxyUserTypeEnum.MASTER.getType());
        merchantDao.insertMerchant(info);
        Merchantinfo merchantinfo = new Merchantinfo();
        merchantinfo.setLinkMobile(info.getLinkMobile());
        List<Merchantinfo> merchantinfoList = merchantDao.selectMerchantinfo(merchantinfo);
        Integer merchId = merchantinfoList.get(0).getId();
        proxyUser.setMerchId(merchId);
        proxyUserDao.insertProxyUser(proxyUser);
        //添加商户账户
        MerchantAccount merchantAccount = new MerchantAccount();
        merchantAccount.setMerchId(merchId);
        merchantAccount.setSurplusCount(0);
        merchantAccount.setCreateBy(info.getCreateBy());
        merchantAccount.setUpdateBy(info.getCreateBy());
        merchantAccount.setUpdateTime(new Date());
        merchantAccount.setCreateTime(new Date());
        merchantAccountDao.insertMerchantAccount(merchantAccount);
        //将所有字段配置进该商户，默认芝麻流程

        MerchFlowNoEnum[] values = MerchFlowNoEnum.values();
        for (MerchFlowNoEnum m:values) {
            String flowNo = m.getFlowNo();
            if(MerchFlowNoEnum.EDUCATION_NEW.getFlowNo().equals(flowNo)){
                continue;
            }
            //默认配置个人信息，芝麻
            FlowOption flowOption= new FlowOption();
            flowOption.setFlowNo(flowNo);
            List<FlowOption> flowOptionList = flowOptionDao.selectFlowOption(flowOption);
            flowOptionList.forEach(f -> {
                MerchantFlowOption merchantFlowOption= new MerchantFlowOption();
                merchantFlowOption.setMerchId(merchId);
                merchantFlowOption.setFlowOptionNo(f.getFlowOptionNo());
                merchantFlowOption.setFlowNo(flowNo);
                merchantFlowOptionDao.insertMerchantFlowOption(merchantFlowOption);
            });
            if(MerchFlowNoEnum.BASE_INFO.getFlowNo().equals(flowNo)){
                continue;
            }
            MerchantinfoFlow merchantinfoFlow = new MerchantinfoFlow();
            merchantinfoFlow.setMerchId(merchId);
            merchantinfoFlow.setFlowNo(flowNo);
            merchantinfoFlow.setFlowStep(AppInit.getCheckflowByFlowNo(flowNo).getStep());
            merchantinfoFlowDao.insertLoanMerchantinfoFlow(merchantinfoFlow);
        }

        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
//        if(info.getCertificateUrl()==null&&info.getAgreementUrl()==null){
//            businessVo.setMessage("商户保存成功，营业执照上传失败,服务协议上传失败");
//        }else if (info.getCertificateUrl()==null){
//            businessVo.setMessage("商户保存成功，营业执照上传失败");
//        }else if (info.getAgreementUrl()==null){
//            businessVo.setMessage("商户保存成功，服务协议上传失败");
//        }else {
//            businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
//        }
        this.updateMerchResourcesMenuWebapp(menuIds,merchId);

//生成短信模板
        for (SmsSendStatueEnum smsSendStatueEnum:SmsSendStatueEnum.values()) {
            SmsTemplate smsTemplate = new SmsTemplate();
            smsTemplate.setMerchId(merchId);
            smsTemplate.setStatus(IsTypeEnum.FALSE.getType());
            smsTemplate.setUpdateUser(manager.getId());
            smsTemplate.setSendStatus(smsSendStatueEnum.getType());
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
                        next_repayment+"，查看账单"+billing_information+"。";
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


        StrBuilder sms = new StrBuilder(ConstansUtils.SMS_CONTENT_PREFIX);
        sms.append(ConstansUtils.SMS_REGISTE_CONTENT_PREFIX).append(info.getMerchantName())
                .append(ConstansUtils.SMS_REGISTE_CONTENT).append(pwd)
                .append(ConstansUtils.SMS_REGISTE_CONTENT_SUFFIX).append(loanMerchUrl);
        try {
            smsComponent.sendSms(sms.toString(),info.getLinkMobile());
        } catch (Exception e) {
            logger.error("商户添加短信发送失败：{}",e);
        }
        return businessVo;
	}

    private void updateMerchResourcesWebapp(String resourcesIds,Integer merchId) {
        if(StringUtils.isNotBlank(resourcesIds)){
            List<String> resourcesIdList = Arrays.asList(resourcesIds.split(","));
            for (String res: resourcesIdList) {
                Integer resId = Integer.valueOf(res);
                MerchResourcesWebapp merchResourcesWebapp = new MerchResourcesWebapp();
                merchResourcesWebapp.setMerchId(merchId);
                merchResourcesWebapp.setResourcesId(resId);
                merchResourcesWebappDao.insert(merchResourcesWebapp);
                ResourcesWebapp resourcesWebapp = resourcesWebappDao.getById(resId);
                //若子节点选中，父节点也必须选中
                if(resourcesWebapp!=null && resourcesWebapp.getParentId().intValue()!=0 &&
                        !resourcesIdList.contains(resourcesWebapp.getParentId().toString())){
                    merchResourcesWebapp = new MerchResourcesWebapp();
                    merchResourcesWebapp.setMerchId(merchId);
                    merchResourcesWebapp.setResourcesId(resourcesWebapp.getParentId());
                    merchResourcesWebappDao.insert(merchResourcesWebapp);
                }
            }
        }
    }

    private List<Integer> updateMerchResourcesMenuWebapp(String menuIds,Integer merchId) {
        if(StringUtils.isNotBlank(menuIds)){
            List<String> resourcesIdList = new ArrayList<>(Arrays.asList(menuIds.split(",")));
            resourcesIdList.add("0");
            List<Integer> resIds = new ArrayList<>();
            for (String res: resourcesIdList) {
                Integer resId = Integer.valueOf(res);
                if(!resId.equals(0)){
                    MerchResourcesWebapp merchResourcesWebapp = new MerchResourcesWebapp();
                    merchResourcesWebapp.setMerchId(merchId);
                    merchResourcesWebapp.setResourcesId(resId);
                    merchResourcesWebappDao.insert(merchResourcesWebapp);
                    resIds.add(merchResourcesWebapp.getResourcesId());
                }
                ResourceMeunWebapp searchResourceMeunWebapp = new ResourceMeunWebapp();
                searchResourceMeunWebapp.setMenuId(resId);
                List<ResourceMeunWebapp> resourceMeunWebapps =
                        resourceMenuWebappDao.listByResourceMeunWebapp(searchResourceMeunWebapp);
                if(resourceMeunWebapps!=null){
                    for (ResourceMeunWebapp resourceMeunWebapp: resourceMeunWebapps) {
                        MerchResourcesWebapp mr = new MerchResourcesWebapp();
                        mr.setMerchId(merchId);
                        mr.setResourcesId(resourceMeunWebapp.getResourcesId());
                        merchResourcesWebappDao.insert(mr);
                        resIds.add(mr.getResourcesId());
                    }
                }

            }
            return resIds;
        }
        return null;
    }

    /*private void updateMerchResourcesWebapp(Integer merchId, Integer resourcesId) {
        MerchResourcesWebapp merchResourcesWebapp = new MerchResourcesWebapp();
        merchResourcesWebapp.setMerchId(merchId);
        merchResourcesWebapp.setResourcesId(resourcesId);
        merchResourcesWebappDao.insert(merchResourcesWebapp);
        ResourcesWebapp search = new ResourcesWebapp();
        search.setParentId(resourcesId);
        search.setType(ResourcesWebappTypeEnum.INTERFACE.getType());
        List<ResourcesWebapp> resourcesWebapps =
                resourcesWebappDao.listByResourcesWebapp(search);
        if(resourcesWebapps!=null){
            for (ResourcesWebapp resourcesWebapp:resourcesWebapps) {
                this.updateMerchResourcesWebapp(merchId,resourcesWebapp.getId());
            }
        }
    }*/


    private String uploadFile(MultipartFile multipartFile,String filename){
        try {
            return ossComponent.uploadFile(filename,multipartFile);
        } catch (FileException e) {
            logger.error("文件上传失败:"+multipartFile.getOriginalFilename());
            return null;
        }
    }


    /**
     * 分页查询商户
     * @param merchantinfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public BusinessVo<BasePageInfoVo<MerchantinfoVo>> selectMerchantByPage(Merchantinfo merchantinfo, Integer pageNum, Integer pageSize) {
        BusinessVo<BasePageInfoVo<MerchantinfoVo>> businessVo = new BusinessVo<>();
        PageHelper.startPage(pageNum, pageSize, true);
        Page<Merchantinfo> page = (Page<Merchantinfo>) merchantDao.selectMerchantinfo(merchantinfo);
        PageInfo<Merchantinfo> loanMerchantinfoVoPageInfo = page.toPageInfo();
        BasePageInfoVo<MerchantinfoVo> basePageInfoVo = assemblyBasePageInfo(loanMerchantinfoVoPageInfo);
        List<MerchantinfoVo> merchantinfoVoList = new ArrayList<>();
        List<Merchantinfo> result = page.getResult();
        if(result!=null&&result.size()!=0){
            merchantinfoListConvert(merchantinfoVoList,result);
        }
        basePageInfoVo.setResultList(merchantinfoVoList);
        businessVo.setData(basePageInfoVo);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    /**
     * list转换方法
     * @param merchantinfoVoList
     * @param result
     */
    private void merchantinfoListConvert(List<MerchantinfoVo> merchantinfoVoList, List<Merchantinfo> result) {
	    result.forEach(merchantinfo -> {
            MerchantinfoVo merchantinfoVo = new MerchantinfoVo();
           merchantinfoConvert(merchantinfoVo,merchantinfo);
            merchantinfoVoList.add(merchantinfoVo);
        });
	}

	private void merchantinfoConvert(MerchantinfoVo merchantinfoVo ,Merchantinfo merchantinfo){
        merchantinfoVo.setId(merchantinfo.getId());
        merchantinfoVo.setMerchantName(merchantinfo.getMerchantName());
        merchantinfoVo.setShortName(merchantinfo.getShortName());
        merchantinfoVo.setEnName(merchantinfo.getEnName());
        merchantinfoVo.setLinkMan(merchantinfo.getLinkMan());
        merchantinfoVo.setLinkMobile(merchantinfo.getLinkMobile());
        merchantinfoVo.setLinkEmail(merchantinfo.getLinkEmail());
        merchantinfoVo.setAddress(merchantinfo.getAddress());
        merchantinfoVo.setScale(merchantinfo.getScale());
        merchantinfoVo.setIndustry(merchantinfo.getIndustry());
        merchantinfoVo.setCertificateName(merchantinfo.getCertificateName()==null?null:merchantinfo.getCertificateName().substring(8));
        merchantinfoVo.setCertificateUrl(merchantinfo.getCertificateUrl());
        merchantinfoVo.setAgreementName(merchantinfo.getAgreementName()==null?null:merchantinfo.getAgreementName().substring(8));
        merchantinfoVo.setAgreementUrl(merchantinfo.getAgreementUrl());
        merchantinfoVo.setStatus(merchantinfo.getStatus());
        merchantinfoVo.setAuditType(merchantinfo.getAuditType());
        merchantinfoVo.setCooperantStopTime(DateUtils.format(merchantinfo.getCooperantStopTime(),DateUtils.yyyyMMdd_hanziformat));
        merchantinfoVo.setCooperantStartTime(DateUtils.format(merchantinfo.getCooperantBeginTime(),DateUtils.yyyyMMdd_hanziformat));
    }

    /**
     * 根据id删除商户
     * @param id
     */
    public void deleteMerchant(String id) {
        //删除oss上的图片
        /*LoanMerchantinfo merchantinfo = loanMerchantMapper.findMerchantinfoByToid(toid);
		if(StringUtils.isNotBlank(merchantinfo.getCertificate_pic())) {
			OssUtils.deleteFile(merchantinfo.getCertificate_pic());
		}*/
        merchantDao.deleteMerchant(id);
    }

    /**
     * 更新商户
     * @param id id
     * @param merchantName merchantName
     * @param shortName shortName
     * @param enName enName
     * @param linkMan linkMan
     * @param linkMobile linkMobile
     * @param linkEmail linkEmail
     * @param address address
     * @param scale scale
     * @param industry industry
     * @param cooperantBeginTime cooperantBeginTime
     * @param cooperantStopTime cooperantStopTime
     * @param certificate certificate
     * @param agreement agreement
     * @param menuIds menuIds
     * @return String
     */
    @Transactional
    public BusinessVo<String> updateMerchant( String token,Integer id,String merchantName,String shortName,
            String enName, String linkMan, String linkMobile, String linkEmail, String address,
            String scale,String industry,String auditType,String cooperantBeginTime,String cooperantStopTime,
            MultipartFile certificate,MultipartFile agreement,String menuIds) {
	    BusinessVo<String> businessVo = new BusinessVo<>();
        StringBuilder key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
        ManageAdmin manager = sessionComponent.getAttributeManageAdmin(key.toString());
        Merchantinfo info = merchantDao.findMerchantinfoByToid(id);
        if(StringUtils.isNotBlank(merchantName)){
            info.setMerchantName(merchantName);
        }
        if(StringUtils.isNotBlank(shortName)){
            info.setShortName(shortName);
        }
        if(StringUtils.isNotBlank(enName)){
            info.setEnName(enName);
        }
        if(StringUtils.isNotBlank(linkMan)){
            info.setLinkMan(linkMan);
        }
        if(StringUtils.isNotBlank(linkMobile)){
            info.setLinkMobile(linkMobile);
        }
        if(StringUtils.isNotBlank(linkEmail)){
            info.setLinkEmail(linkEmail);
        }
        if(StringUtils.isNotBlank(address)){
            info.setAddress(address);
        }
        if(StringUtils.isNotBlank(scale)){
            info.setScale(scale);
        }
        if(StringUtils.isNotBlank(industry)){
            info.setIndustry(industry);
        }
        if(StringUtils.isNotBlank(auditType)){
            info.setAuditType(auditType);
        }
        if(StringUtils.isNotBlank(cooperantBeginTime)){
            info.setCooperantBeginTime(TypeUtils.castToDate(cooperantBeginTime));
        }
        if(StringUtils.isNotBlank(cooperantStopTime)){
            info.setCooperantStopTime(TypeUtils.castToDate(cooperantStopTime));
        }
        String agreementNameUrl="";
        String certificateUrl="";
        String certificateName = null;
        String agreementNameName = null;
        if(null != certificate && !certificate.isEmpty()) {
            certificateName = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8)+certificate.getOriginalFilename();
            certificateUrl= uploadFile(certificate,certificateName);
            info.setCertificateName(certificateName);
            info.setCertificateUrl(certificateUrl);
        }
        if(null != agreement && !agreement.isEmpty()) {
            agreementNameName = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8)+agreement.getOriginalFilename();
            agreementNameUrl= uploadFile(agreement,agreementNameName);
            info.setAgreementName(agreementNameName);
            info.setAgreementUrl(agreementNameUrl);
        }
        info.setStatus(MerchStatusEnum.USING.getType());
        info.setUpdatetime(new Date());
        merchantDao.updateMerchant(info);
        //生成短信模板
        /*for (SmsSendStatueEnum smsSendStatueEnum:SmsSendStatueEnum.values()) {
            SmsTemplate smsTemplate = new SmsTemplate();
            smsTemplate.setMerchId(id);

            smsTemplate.setSendStatus(smsSendStatueEnum.getType());
            smsTemplate = smsTemplateDao.getBySmsTemplate(smsTemplate);
            if(smsTemplate==null){
                smsTemplate = new SmsTemplate();
                smsTemplate.setMerchId(id);
                smsTemplate.setSendStatus(smsSendStatueEnum.getType());
                smsTemplate.setStatus(IsTypeEnum.FALSE.getType());
                smsTemplate.setUpdateUser(manager.getId());
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
        }*/
        if(StringUtils.isNotBlank(menuIds)){

            MerchResourcesWebapp searchMerchResources = new MerchResourcesWebapp();
            searchMerchResources.setMerchId(id);
            List<MerchResourcesWebapp> merchResourcesWebapps =
                    merchResourcesWebappDao.listByMerchResourcesWebapp(searchMerchResources);
            if(merchResourcesWebapps!=null){
                for (MerchResourcesWebapp merchResourcesWebapp:merchResourcesWebapps) {
                    merchResourcesWebappDao.delete(merchResourcesWebapp.getId());
                }
            }

            List<Integer> resIds = this.updateMerchResourcesMenuWebapp(menuIds, id);


           // List<String> resourcesIdList = Arrays.asList(menuIds.split(","));
            RoleResourcesWebapp searchRoleResources = new RoleResourcesWebapp();
            searchRoleResources.setMerchId(id);
            List<RoleResourcesWebapp> roleResourcesWebapps = roleResourcesWebappDao.listByRoleResourcesWebapp(searchRoleResources);
            if(roleResourcesWebapps!=null){
                for (RoleResourcesWebapp roleResourcesWebapp:roleResourcesWebapps) {
                    if(CollectionUtil.isNotBlank(resIds) &&
                            !resIds.contains(roleResourcesWebapp.getResourcesId())){
                        roleResourcesWebappDao.delete(roleResourcesWebapp.getId());
                    }
                }
            }

        }


        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }


    public BusinessVo<String> updateStatus(Integer id, String status) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Merchantinfo merchantinfo= new Merchantinfo();
        merchantinfo.setId(id);
        merchantinfo.setStatus(status);
        merchantDao.updateMerchant(merchantinfo);
        //账户操作
        if(ProxyUserStatusEnum.EFFECTIVE.getType().equals(status)){
            //只开启主账户
            proxyUserDao.updatePorxyUserStatus(id,ProxyUserTypeEnum.MASTER.getType(),status);
        }else {
            proxyUserDao.updatePorxyUserStatus(id,null,status);
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;
    }

    public BusinessVo<List<MerchantNameVo>> getAllMerchantName(String merchName) {
        BusinessVo<List<MerchantNameVo>> businessVo = new BusinessVo<>();
        List<MerchantNameVo> merchantNameVoList= merchantDao.getAllMerchantName(merchName);
        businessVo.setData(merchantNameVoList);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return businessVo;

    }
}
