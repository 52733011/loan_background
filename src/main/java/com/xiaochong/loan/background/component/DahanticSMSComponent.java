package com.xiaochong.loan.background.component;

import com.alibaba.fastjson.JSONObject;
import com.dahantc.api.sms.json.JSONHttpClient;
import com.xiaochong.loan.background.exception.SMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by yehao on 17/6/16.
 */
@Component("dahanticSMSComponent")
public class DahanticSMSComponent {

    public static final Logger logger = LoggerFactory.getLogger(DahanticSMSComponent.class);

    @Value("${dahantic.account}")
    private String  ACCOUNT;
    @Value("${dahantic.password}")
    private String  PASSWORD;
    @Value("${dahantic.sign}")
    private String  SIGN;
    @Value("${dahantic.url}")
    private String  URL;
    @Value("${dahantic.retryTimes}")
    private Integer RETRY_TIMES;

    /**
     * 大汉三通发短信接口
     *
     * @param phone 手机号
     * @param msg   短信消息
     * @return 是否发送成功
     * @throws SMSException 如果发送短信失败,则抛短信异常
     */
    public boolean sendSMS(String phone, String msg) throws SMSException {
        logger.info("向[{}]发送短信[{}]", phone, msg);
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(msg)) {
            throw new SMSException("参数不能为空!");
        }

        try {
            JSONHttpClient jsonHttpClient = new JSONHttpClient(URL);
            jsonHttpClient.setRetryCount(RETRY_TIMES);
            String res = jsonHttpClient.sendSms(ACCOUNT, PASSWORD, phone, msg, "【小虫背调】", null);
            logger.info("response:{}", res);
            JSONObject jsonObject = JSONObject.parseObject(res);
            Integer    result     = jsonObject.getInteger("result");
            return !(null == result || result != 0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SMSException();
        }
    }
}
