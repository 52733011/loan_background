package com.xiaochong.loan.background.service;

import com.xiaochong.loan.background.component.SMSComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;


@Service("VerificationService")
public class VerificationService{

	private Logger logger = LoggerFactory.getLogger(VerificationService.class);

	@Resource(name = "smsComponent")
	private SMSComponent smsComponent;

	@Resource(name = "sessionComponent")
	private SessionComponent sessionComponent;

	public BusinessVo<Boolean> sendSmsCode(String phone, Integer type) throws DataDisposeException, OKhttpException {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		//生成六位随机数
		Random random = new Random();
		StringBuffer sbContent = new StringBuffer();
		for(int i=0;i<6;i++){
			sbContent.append(random.nextInt(10));
		}
		sessionComponent.setAttribute("SMS-"+phone+"-"+type,sbContent.toString(),300L);

		Boolean sendsmsState = smsComponent.sendSms("验证码:"+sbContent+"，请在五分钟内输入。",phone);

		businessVo.setData(sendsmsState);
		if(sendsmsState){
			logger.info("发送验证码",sendsmsState);
		}else{
			logger.warn("发送验证码失败",sendsmsState);
			businessVo.setCode(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_SEND_ERROR_DESC);
		}
		return businessVo;
	}

	public BusinessVo<Boolean> checkVerification(String phone, String sms, Integer type) {
		BusinessVo<Boolean> businessVo = new BusinessVo<>();
		businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
		businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
		String code = sessionComponent.getAttribute("SMS-" + phone + "-" + type);
		if(sms.equals(code)){
			businessVo.setData(true);
		}else {
			businessVo.setCode(BusinessConstantsUtils.VERIFICATION_ERROR_CODE);
			businessVo.setMessage(BusinessConstantsUtils.VERIFICATION_ERROR_DESC);
		}
		return businessVo;
	}
}
