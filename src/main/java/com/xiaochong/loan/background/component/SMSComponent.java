package com.xiaochong.loan.background.component;

import com.xiaochong.loan.background.dao.SmsSendLogDao;
import com.xiaochong.loan.background.entity.po.SmsSendLog;
import com.xiaochong.loan.background.entity.po.SmsTemplate;
import com.xiaochong.loan.background.entity.vo.LoanSmsTemplate;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.exception.SMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ray.liu on 2017/4/13.
 */
@Component("smsComponent")
public class SMSComponent {

    private Logger logger = LoggerFactory.getLogger(SMSComponent.class);

    @Value("${background.yimei.cdkey}")
    private String cdKey;

    @Value("${background.yimei.password}")
    private String password;

    @Value("${background.yimei.sendsmsUrl}")
    private String sendSmsUrl;

    @Value("${background.yimei.getreportUrl}")
    private String getReportUrl;

    @Resource(name = "smsSendLogDao")
    private SmsSendLogDao smsSendLogDao;

    @Resource()
    private DahanticSMSComponent dahanticSMSComponent;

    private static String seqId;


    public Boolean sendSms(String content,String phone) throws DataDisposeException, OKhttpException {
        Boolean result = false;
        try {
            result = dahanticSMSComponent.sendSMS(phone, content);
        } catch (SMSException e) {
            logger.error(e.getMessage(),e);
        }
        return result;
//        String message = ConstansUtils.SMS_CONTENT_PREFIX + content;
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("cdkey", cdKey);
//        paramsMap.put("password", password);
//        paramsMap.put("phone", phone);
//        paramsMap.put("message", message);
//        seqId = Long.toString(System.currentTimeMillis());
//        paramsMap.put("seqid", seqId);
//        String params = ParamsUtils.mapToString(paramsMap);
//        logger.info("调用短信接口：{}",sendSmsUrl + "?" + params);
//        String requestResult = OkHttpUtils.get(sendSmsUrl + "?" + params);
//        String result = AnalysisXml.analysisXml(requestResult);
//        logger.info("短信接口回执：{}", result);
//        return "0".equals(result);
    }



    public Boolean sendLoanSms(List<LoanSmsTemplate> loanSmsTemplates,
                               SmsTemplate smsTemplate, String phone) throws DataDisposeException, OKhttpException {
        Boolean result = false;
        String content = smsTemplate.getContent();
        for (LoanSmsTemplate loanSmsTemplate:loanSmsTemplates) {
            String name = "/%"+loanSmsTemplate.getSmaTagEnum().getName()+"%/";
            content = content.replaceAll(name,
                   loanSmsTemplate.getParam()!=null?loanSmsTemplate.getParam().toString():"");
        }
        try {
            result = dahanticSMSComponent.sendSMS(phone, content);
            if(result){
                SmsSendLog smsSendLog = new SmsSendLog();
                smsSendLog.setTemplateId(smsTemplate.getId());
                smsSendLog.setMerchId(smsTemplate.getMerchId());
                smsSendLog.setSendContent(content);
                smsSendLogDao.insert(smsSendLog);
            }
        } catch (SMSException e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }



}
