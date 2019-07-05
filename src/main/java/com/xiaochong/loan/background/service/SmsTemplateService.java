package com.xiaochong.loan.background.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.dao.MerchantDao;
import com.xiaochong.loan.background.dao.ProxyUserDao;
import com.xiaochong.loan.background.dao.SmsSendLogDao;
import com.xiaochong.loan.background.dao.SmsTemplateDao;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.po.SmsTemplate;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.enums.SmaTagEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service("smsTemplateService")
public class SmsTemplateService extends BaseService{

	private Logger logger = LoggerFactory.getLogger(SmsTemplateService.class);

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	@Resource(name = "proxyUserDao")
	private ProxyUserDao proxyUserDao;

	@Resource(name = "smsTemplateDao")
	private SmsTemplateDao smsTemplateDao;

	@Resource(name = "smsSendLogDao")
	private SmsSendLogDao smsSendLogDao;

	@Resource(name = "merchantDao")
	private MerchantDao merchantDao;

	public BusinessVo<Boolean> insertSmsTemplate(String token, String content, String sendStatus,String status) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("SmsTemplateService.insertSmsTemplate登录失效！token:{}",token);
			return businessVo;
		}

		SmsTemplate smsTemplate = new SmsTemplate();
		smsTemplate.setMerchId(proxyUser.getMerchId());
		smsTemplate.setContent(content);
		smsTemplate.setSendStatus(sendStatus);
		smsTemplate.setStatus(status);
		if(smsTemplateDao.insert(smsTemplate)!=1){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("SmsTemplateService.insertSmsTemplate修改数据失败！smsTemplate:{}",smsTemplate);
			return businessVo;
		}
		businessVo.setData(true);
		return businessVo;
	}


	public BusinessVo<Boolean> updateSmsTemplate(String token,Integer id, String content,String status) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("SmsTemplateService.updateSmsTemplate登录失效！token:{}",token);
			return businessVo;
		}
		SmsTemplate smsTemplate = smsTemplateDao.getById(id);
		if(StringUtils.isNotBlank(content)){
			smsTemplate.setContent(content);
		}
		if(StringUtils.isNotBlank(status)){
			smsTemplate.setStatus(status);
		}

		if(smsTemplateDao.update(smsTemplate)!=1){
			businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
			businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
			logger.warn("SmsTemplateService.updateSmsTemplate修改数据失败！smsTemplate:{}",smsTemplate);
			return businessVo;
		}
		businessVo.setData(true);
		return businessVo;

	}

	public BusinessVo<BasePageInfoVo<SmaTemplateVo>> smsTemplatePage(String token, Integer pageNum, Integer pageSize) {
		BusinessVo<BasePageInfoVo<SmaTemplateVo>> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("SmsTemplateService.smsTemplatePage登录失效！");
			return businessVo;
		}
		Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(proxyUser.getMerchId());
		if(merchantinfo==null){
			businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
			businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
			logger.warn("SmsTemplateService.smsTemplatePage找不到商户！merchId",proxyUser.getMerchId());
			return businessVo;
		}
		PageHelper.startPage(pageNum, pageSize,true);
		Page<SmaTemplateVo> list =
				(Page<SmaTemplateVo>)smsTemplateDao.smsTemplatePage(proxyUser.getId());
		List<SmaTemplateVo> result = list.getResult();
		for (SmaTemplateVo smaTemplateVo: result) {
			smaTemplateVo.setSample(this.getContentSample(merchantinfo,smaTemplateVo.getContent()));
		}

		//拼装成返回类
		PageInfo<SmaTemplateVo> pageInfo = list.toPageInfo();
		//拼装分页类
		BasePageInfoVo<SmaTemplateVo> basePageInfoVo = assemblyBasePageInfo(pageInfo);
		basePageInfoVo.setResultList(result);
		businessVo.setData(basePageInfoVo);
		return businessVo;
	}


	public BusinessVo<SmaSendCountVo> getSmaSendCount(String token) {
		BusinessVo<SmaSendCountVo> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("SmsTemplateService.getSmaSendCount登录失效！");
			return businessVo;
		}
		BigDecimal smsPrice = new BigDecimal(0.035);
		SmaSendCountVo smaSendCountVo = new SmaSendCountVo();
		smaSendCountVo.setMerchId(proxyUser.getMerchId());
		long monthCount = smsSendLogDao.countByMerchIdMonth(proxyUser.getMerchId());
		smaSendCountVo.setMonthCount(monthCount);
		smaSendCountVo.setMonthPrice(smsPrice.multiply(new BigDecimal(monthCount)));
		long allCount = smsSendLogDao.countByMerchIdAll(proxyUser.getMerchId());
		smaSendCountVo.setAllCount(allCount);
		smaSendCountVo.setAllPrice(smsPrice.multiply(new BigDecimal(allCount)));
		businessVo.setData(smaSendCountVo);
		return businessVo;
	}

	public BusinessVo<String> getSample(String token, String content) {
		BusinessVo<String> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
		ProxyUser proxyUser = StringUtils.isNotBlank(proxyUserId)?
				proxyUserDao.getById(Integer.parseInt(proxyUserId)):null;
		if(proxyUser==null){
			businessVo.setCode(BusinessConstantsUtils.LOGIN_INVALID_CODE);
			businessVo.setMessage(BusinessConstantsUtils.LOGIN_INVALID_DESC);
			logger.warn("SmsTemplateService.getSample登录失效！");
			return businessVo;
		}

		Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(proxyUser.getMerchId());
		if(merchantinfo==null){
			businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
			businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
			logger.warn("SmsTemplateService.getSample找不到商户！merchId",proxyUser.getMerchId());
			return businessVo;
		}
		content = this.getContentSample(merchantinfo,content);

		businessVo.setData(content);
		return businessVo;
	}

	private String getContentSample(Merchantinfo merchantinfo, String content) {
		String company_name = "/%"+ SmaTagEnum.COMPANY_NAME.getName()+"%/";
		content = content.replaceAll(company_name,merchantinfo.getMerchantName());
		String borrowing_information = "/%"+ SmaTagEnum.BORROWING_INFORMATION.getName()+"%/";
		content = content.replaceAll(borrowing_information,"借款8000元分期12月");
		String bank_card_no = "/%"+ SmaTagEnum.BANK_CARD_NO.getName()+"%/";
		content = content.replaceAll(bank_card_no,"0000");
		String next_repayment = "/%"+ SmaTagEnum.NEXT_REPAYMENT.getName()+"%/";
		content = content.replaceAll(next_repayment,"第一期金额为332.43元，最后还款日为9月10日");
		String overdue_days = "/%"+ SmaTagEnum.OVERDUE_DAYS.getName()+"%/";
		content = content.replaceAll(overdue_days,"第一期已逾期X天，今日当期应还432.32元");
		String billing_information = "/%"+ SmaTagEnum.BILLING_INFORMATION.getName()+"%/";
		content = content.replaceAll(billing_information,"查看账单：http://XXXXXX");
		String repayment = "/%"+ SmaTagEnum.REPAYMENT.getName()+"%/";
		content = content.replaceAll(repayment,"第一期332.43元已还款成功，第二期金额为332.43元，最后还款日为9月10日");
		return content;
	}
}
