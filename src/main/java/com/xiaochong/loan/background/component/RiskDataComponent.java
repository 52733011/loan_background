package com.xiaochong.loan.background.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.vo.RiskCallbackVo;
import com.xiaochong.loan.background.entity.vo.RiskResultVo;
import com.xiaochong.loan.background.entity.vo.yys.YysResultVoData;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.utils.JsonUtils;
import com.xiaochong.loan.background.utils.OkHttpUtils;
import com.xiaochong.loan.background.utils.RiskConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component("riskDataComponent")
public class RiskDataComponent {

    Logger logger = LoggerFactory.getLogger(RiskDataComponent.class);

    @Value("${risk.yys.certificate_url}")
    private String certificateUrl;

    @Value("${risk.app_account}")
    private String appAccount;

    @Value("${risk.secret_key}")
    private String riskSecretKey;

    @Value("${risk.education.url}")
    private String educationAuthUrl;

    @Value("${risk.zhimainfo.url}")
    private String zhimaInfoUrl;

    @Value("${risk.user.info_url}")
    private String userInfoUrl;

    @Value("${risk.unionPayAuth_url}")
    private String unionPayAuthUrl;

    @Value("${risk.yys.verify_code_url}")
    private String verifyCodeUrl;

    @Value("${risk.user.contact_url}")
    private String userContactUrl;

    @Value("${risk.tongdun.secret_key}")
    private String tongdunSecretKey;

    @Value("${risk.tongdun.loanevent_type}")
    private String tongdunLoanEventType;

    @Value("${risk.tongdun.loan_event_url}")
    private String loanEventUrl;

    @Value("${risk.submit_order_url}")
    private String riskSubmitUrl;

    @Value("${risk.credit.report.url}")
    private String creditReportUrl;

    @Value("${risk.zhongant_validate_mobile_url}")
    private String zhonganValidateMobileURL;

    @Value("${risk.yys.call_count_url}")
    private String yysCallCountUrl;

    //学信网
    @Value("${risk.query_education_url}")
    private String queryEducationUrl;
    //验证码查询
    @Value("${risk.query_with_captcha_url}")
    private String queryWithCaptchaUrl;
    //重新获取验证码
    @Value("${risk.change_aptcha_url}")
    private String changeCaptchaUrl;

    @Value("${risk.identity_image_url}")
    private String identityImageUrl;
    @Value("${risk.identity_image_prefix}")
    private String identityImagePrefix;

    public static String token = "";


    /**
     * risk学信网认证结果
     *
     * @param name         姓名
     * @param id_card      身份证
     * @param mobile       电话号码
     * @param account      学信账号
     * @param password     学信密码
     * @param callback_set 学信网回调地址
     * @return 学信网认证接口请求状态
     */
    public RiskResultVo educationAction(String name, String id_card, String mobile, String account, String password, String callback_set) {
        RiskResultVo riskResultVo = new RiskResultVo();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        paramsMap.put("id_card", id_card);
        paramsMap.put("mobile", mobile);
        paramsMap.put("account", account);
        paramsMap.put("password", password);
        paramsMap.put("callback_set", callback_set);
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key", riskSecretKey);
        paramsMap.put("token", token);
        try {
            String post = OkHttpUtils.postTwoMinutes(educationAuthUrl, paramsMap);
            JSONObject json = JSON.parseObject(post);
            String code = json.getString("code");
            riskResultVo.setCode(code);
            if (code.equals("105")) {
                // 信息提交成功
                riskResultVo.setMessage("学信提交成功");
            } else if (code.equals("104")) {
                riskResultVo.setMessage("学信账号密码错误");
            } else {
                riskResultVo.setMessage("学信提交失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            riskResultVo.setMessage("false");
            riskResultVo.setMessage("学信请求错误");
        }
        return riskResultVo;
    }


    /**
     * risk芝麻认证
     *
     * @param name         姓名
     * @param certNo       身份证号
     * @param phone        手机号
     * @param callback_set 回调参数
     * @return 芝麻认证请求结果
     */
    public RiskResultVo<String> zhimaAction(String name, String certNo,
                                    String phone, String callback_set) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        paramsMap.put("certNo", certNo);
        paramsMap.put("phone", phone);
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key", riskSecretKey);
        paramsMap.put("callback_set", callback_set);
        paramsMap.put("token", token);
        System.out.println("paramsMap: " + paramsMap);
        RiskResultVo<String> result = new RiskResultVo<>();
        try {
            String post = OkHttpUtils.post(zhimaInfoUrl, paramsMap);
            JSONObject json = JSON.parseObject(post);
            String issuccess = json.getString("success");
            String data = json.getString("data");
            result.setMessage(issuccess);
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("false");
            result.setData("芝麻请求错误");
        }
        return result;
    }

    public RiskResultVo subUserInfo(String id_card, String real_name, String phone) throws OKhttpException {
        RiskResultVo resultVo = new RiskResultVo();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("id_card", id_card);
        JSONObject object = new JSONObject();
        object.put("idCard", id_card);
        object.put("realName",real_name);
        object.put("mobile", phone);
        object.put("status", "");
        object.put("headImg", "");
        object.put("sex", "");
        object.put("nation", "");
        object.put("cityId", "");
        object.put("address", "");
        object.put("email", "");
        object.put("qq", "");
        object.put("wx", "");
        object.put("birthday", "");
        object.put("familyPhone", "");
        object.put("maxDegree", "");
        paramMap.put("user_info", object.toJSONString());
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key",riskSecretKey);
        String result;
        try {
            result = OkHttpUtils.post(userInfoUrl, paramMap);
            logger.info("URL:【"+userInfoUrl+"】"+result);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            resultVo.setCode(code);
            if(code.equals("100")){
                resultVo.setMessage("用户信息提交成功");
            }else{
                resultVo.setMessage("用户信息提交失败");
            }
        } catch (OKhttpException e) {
            resultVo.setMessage("用户信息提交异常");
            logger.error(e.getMessage(),e);
            throw e;
        }
        return resultVo;
    }

    public RiskResultVo<String> unionPayAuthAction(String name, String id_card,
                                           String mobile,String accNo) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        paramsMap.put("id_card", id_card);
        paramsMap.put("mobile", mobile);
        paramsMap.put("accNo",accNo);

        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key", riskSecretKey);
        paramsMap.put("token", token);
        RiskResultVo<String> result = new RiskResultVo<>();
        try {
            String post = OkHttpUtils.postTwoMinutes(unionPayAuthUrl, paramsMap);
            JSONObject json = JSON.parseObject(post);
            String code = json.getString("code");
            if (code.equals(RiskConstantsUtils.UNIONPAY_ERROR)) {
                // 失败
                result.setMessage("持卡人身份信息、证件类型、手机号、有效期或CVN2验证不一致");
            } else if (code.equals(RiskConstantsUtils.UNIONPAY_SUCCESS)) {
                result.setMessage("绑卡成功");
            }else {
                result.setMessage("系统维护中");
            }
            result.setCode(code);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("请求银联异常");
            result.setData("请求银联异常");
        }
        return result;
    }

    public RiskResultVo<YysResultVoData> yysCertificate(String id_card, String user_name,
                                          String mobile_no, String password, String callback_set)
            throws UnsupportedEncodingException,
            OKhttpException, DataDisposeException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobile_no", mobile_no);
        paramMap.put("identity_card", id_card);
        paramMap.put("password", password);
        paramMap.put("name", user_name);
        paramMap.put("callback_set", callback_set);
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key", riskSecretKey);
        paramMap.put("token", token);
        System.out.println(appAccount);
        String apiResult = OkHttpUtils.postTwoMinutes(certificateUrl,
                paramMap);
        String code = JsonUtils.getNodeValue(apiResult,
                RiskConstantsUtils.YYS_NODE_CODE);
        return padYysResult(code, apiResult);
    }

    private  RiskResultVo<YysResultVoData> padYysResult(String code,String apiResult) {
        RiskResultVo<YysResultVoData> baseResultVo = new RiskResultVo<>();
        if (RiskConstantsUtils.YYS_CODE_104.equals(code)) {
            // 处理中
            String data = JsonUtils.getNodeValue(apiResult,
                    RiskConstantsUtils.YYS_NODE_DATA);
            YysResultVoData yysResultVoData = new YysResultVoData();
            yysResultVoData.setTrace_id(JsonUtils.getNodeValue(apiResult,
                    RiskConstantsUtils.YYS_NODE_TRACEID));
            yysResultVoData.setGuide_code(JsonUtils.getNodeValue(data,
                    RiskConstantsUtils.YYS_NODE_GUIDE_CODE));
            yysResultVoData.setGuide_message(JsonUtils.getNodeValue(data,
                    RiskConstantsUtils.YYS_NODE_GUIDE_MESSAGE));
            yysResultVoData.setTask_id(JsonUtils.getNodeValue(data,
                    RiskConstantsUtils.YYS_NODE_TASKID));
            yysResultVoData.setImg_code(JsonUtils.getNodeValue(data,
                    RiskConstantsUtils.YYS_NODE_IMGCODE));
            baseResultVo.setData(yysResultVoData);
        }
        baseResultVo.setCode(code);
        baseResultVo.setMessage(JsonUtils.getNodeValue(apiResult,
                RiskConstantsUtils.YYS_NODE_MESSAGE));
        return baseResultVo;
    }

    public RiskResultVo<YysResultVoData> yysVerifyCode(String task_id, String mobile_no,
                                                       String auth_code, String identity_card,
                                                       String trace_id, String callback_set) throws OKhttpException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("task_id", task_id);
        paramMap.put("mobile_no", mobile_no);
        paramMap.put("auth_code", auth_code);
        paramMap.put("identity_card", identity_card);
        paramMap.put("callback_set", callback_set);
        paramMap.put("trace_id", trace_id);
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key", riskSecretKey);
        paramMap.put("token", token);
        String apiResult = OkHttpUtils
                .postTwoMinutes(verifyCodeUrl, paramMap);
        String code = JsonUtils.getNodeValue(apiResult,
                RiskConstantsUtils.YYS_NODE_CODE);
        return padYysResult(code, apiResult);
    }


    /**
     *
     * @param id_card   用户身份证号码（非联系人）
     * @param type      参照ConstansUtils.CONTACT_TYPE_FATHER = "1";
     * @param name      联系人姓名
     * @param mobile    联系人手机号码
     * @return
     */
    public JSONObject padContactUser(String id_card,String type,String name,String mobile,String address){
        JSONObject object = new JSONObject();
        object.put("idCard", id_card);
        object.put("type", type);
        object.put("name", name);
        object.put("mobile", mobile);
        object.put("address", address);
        object.put("status", "");
        object.put("clientName", "");
        return object;
    }

    /**
     *
     * @param id_card
     * @param jsonArray   调用  padContactUser方法组装json
     * @return
     */
    public RiskResultVo<String> subUserConatct(String id_card,JSONArray jsonArray){
        RiskResultVo<String> resultVo = new RiskResultVo<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("id_card", id_card);
        paramMap.put("user_contact", jsonArray.toJSONString());
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key",riskSecretKey);
        String result;
        try {
            result = OkHttpUtils.post(userContactUrl, paramMap);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            resultVo.setCode(code);
            if(code.equals(RiskConstantsUtils.SUBMIT_CONTACT_CODE_SUCCESS)){
                resultVo.setMessage("用户联系人提交成功");
            }else{
                resultVo.setMessage("用户联系人提交失败");
            }
        } catch (OKhttpException e) {
            e.printStackTrace();
            resultVo.setMessage("用户联系人提交异常");
        }
        return resultVo;
    }


    /**
     * 同盾贷款事件
     * @param idCard 身份证
     * @param accountName 用户姓名
     * @param phone 手机号
     * @param ipAddress 客户端ip地址
     * @return 结果集
     */
    public RiskResultVo<String> tongdunLoanEvent(String idCard,String accountName,String phone,String ipAddress){
        RiskResultVo<String> resultVo = new RiskResultVo<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key", riskSecretKey);
        paramMap.put("id_number",idCard);
        paramMap.put("account_name",accountName);
        paramMap.put("account_mobile",phone);
        paramMap.put("secret_key",tongdunSecretKey);
        paramMap.put("event_id",tongdunLoanEventType);
        paramMap.put("ip_address",ipAddress);
        try {
            String result=OkHttpUtils.post(loanEventUrl, paramMap);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            resultVo.setCode(code);
            if(code.equals("100")){
                resultVo.setMessage("同盾借款事件成功");
            }else{
                resultVo.setMessage("同盾借款事件失败");
            }
        } catch (OKhttpException e) {
            logger.error(e.getMessage(),e);
            resultVo.setMessage("同盾借款事件异常");
        }
        return resultVo;
    }



    /**
     * risk订单提交接口
     * @param idCard 身份证
     * @param clientIp 客户端ip
     * @param callbackUrl 回调地址
     * @param orderInfo 订单信息
     * @return 调用结果
     */
    public RiskResultVo<String> submitOrder(String idCard,String clientIp,String callbackUrl,String orderInfo) {
        RiskResultVo<String> resultVo = new RiskResultVo<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key", riskSecretKey);
        paramMap.put("client_ip",clientIp);
        paramMap.put("id_card",idCard);
        paramMap.put("order_info",orderInfo);
        RiskCallbackVo callbackVo=new RiskCallbackVo();
        callbackVo.setCallback_url(callbackUrl);
        String callback_set=JSON.toJSONString(callbackVo);
        paramMap.put("callback_set",callback_set);
        try {
            String result=OkHttpUtils.post(riskSubmitUrl, paramMap);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            resultVo.setCode(code);
            if(code.equals("100")){
                resultVo.setMessage("订单提交成功");
            }else if(code.equals("101")){
                resultVo.setMessage("订单提交失败");
            }else if(code.equals("102")){
                resultVo.setMessage("参数错误");
            }else if(code.equals("108")){
                resultVo.setMessage("参数长度错误");
            }
        } catch (OKhttpException e) {
            logger.error(e.getMessage(),e);
            resultVo.setMessage("出现异常");
        }
        return resultVo;
    }

    public boolean validateMobile(String name, String card, String phone) throws OKhttpException {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key", riskSecretKey);
        paramsMap.put("name", name);
        paramsMap.put("idCard", card);
        paramsMap.put("cellPhone", phone);
        String post = OkHttpUtils.post(zhonganValidateMobileURL, paramsMap);

        JSONObject riskResultjsonObject = JSON.parseObject(post);
        if(riskResultjsonObject==null){
            logger.error("三要素验证返回失败：{}",post);
            return false;
        }
        logger.info("三要素验证：{}",post);
        if(!RiskConstantsUtils.ZHONGAN_RESULT_CODE_SUCCESS.equals(
                riskResultjsonObject.getString("code"))){
            return false;
        }
        JSONObject riskDatajsonObject = riskResultjsonObject.getJSONObject("data");
//        if(RiskConstantsUtils.ZHONGAN_DATA_ERRORCODE_SUCCESS.equals(riskDatajsonObject.getString("errorCode"))){
//            if("Y".equals(riskDatajsonObject.getString("checkResult"))){
//                return true;
//            }
//        }
//        return false;
        if(RiskConstantsUtils.ZHONGAN_DATA_ERRORCODE_SUCCESS.equals(riskDatajsonObject.getString("errorCode"))){
           if("N".equals(riskDatajsonObject.getString("checkResult"))){
                return false;
            }
        }
        return true;
    }


    /**
     * 获取risk报告
     * @param idCard 身份证
     * @param order_num 订单编号
     * @return 背调报告结果集
     */
    public RiskResultVo<String> getCreditReport(String idCard,String order_num) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("idCard", idCard);
        paramsMap.put("order_num", order_num);
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key", riskSecretKey);

        RiskResultVo<String> result = new RiskResultVo<>();
        try {
            String post = OkHttpUtils.post(creditReportUrl, paramsMap);
            JSONObject json = JSON.parseObject(post);
            String code = json.getString("code");
            String data = json.getString("data");
            String message = json.getString("msg");
            result.setCode(code);
            result.setMessage(message);
            result.setData(data);
        } catch (OKhttpException e) {
            result.setMessage("获取报告接口出现异常");
            logger.error(e.getMessage(),e);
        }
        return result;
    }


    /**
     * 运营商通话记录
     * @param id_card 身份证号
     * @return 查询结果
     */
    public RiskResultVo<String> getYysCallCount(String id_card) {
        RiskResultVo<String> resultVo = new RiskResultVo<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("idCard", id_card);
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key",riskSecretKey);
        String result;
        try {
            result = OkHttpUtils.post(yysCallCountUrl, paramMap);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            String data=json.getString("data");
            if(code.equals("1000")){
                resultVo.setCode("100");
                resultVo.setData(data);
                resultVo.setMessage("查询运营商通话记录成功");
            }else{
                resultVo.setCode("101");
                resultVo.setData(data);
                resultVo.setMessage("查询运营商通话记录失败");
                logger.error("运营商通话记录查询失败：{}","101");
            }
        } catch (OKhttpException e) {
            resultVo.setMessage("查询运营商通话记录接口出现异常");
            logger.error(e.getMessage(),e);
        }
        return resultVo;
    }

    /**
     * 学信网查询接口 返回2005需要再次调用 学信网使用验证码查询接口
     * @param idCard
     * @param userName
     * @param password
     * @return
     * @throws OKhttpException
     */
    public RiskResultVo<String> queryEducation(String idCard,String userName,String password)  {
        RiskResultVo<String> riskResultVo= new RiskResultVo<>();
        try{
            Map<String,String> params= new HashMap<>();
            params.put("idCard",idCard);
            params.put("userName",userName);
            params.put("password",password);
            params.put("app_account",appAccount);
            params.put("risk_secret_key",riskSecretKey);
            String response = OkHttpUtils.halfMinutes(queryEducationUrl, params);
            JSONObject jsonObject = JSON.parseObject(response);
            riskResultVo.setCode(jsonObject.getString("code"));
            riskResultVo.setMessage(jsonObject.getString("message"));
            riskResultVo.setData(jsonObject.getString("data"));
            logger.info("新学信返回参数："+riskResultVo);
        }catch (OKhttpException e) {
            riskResultVo.setMessage("学信网调用异常");
            logger.error("学信网接口调用异常:{}",e);
        }
        return riskResultVo;
    }

    /**
     * 学信网使用验证码查询接口
     * @param idCard
     * @param captcha
     * @param traceId
     * @return
     * @throws OKhttpException
     */
    public RiskResultVo<String> queryWithCaptcha(String idCard,String captcha,String traceId)  {
        RiskResultVo<String> riskResultVo= new RiskResultVo<>();
        try{
            Map<String,String> params= new HashMap<>();
            params.put("idCard",idCard);
            params.put("captcha",captcha);
            params.put("traceId",traceId);
            params.put("app_account",appAccount);
            params.put("risk_secret_key",riskSecretKey);
            String response = OkHttpUtils.post(queryWithCaptchaUrl, params);
            JSONObject jsonObject = JSON.parseObject(response);
            riskResultVo.setCode(jsonObject.getString("code"));
            riskResultVo.setMessage(jsonObject.getString("message"));
            riskResultVo.setData(jsonObject.getString("data"));
            logger.info("risk返回数据：{}",riskResultVo);
        }catch (OKhttpException e) {
            riskResultVo.setMessage("学信网使用验证码查询异常");
            logger.error("学信网使用验证码查询接口:{}",e);
        }
        return riskResultVo;
    }

    /**
     * 学信网重新获取验证码
     * @param idCard idCard
     * @return String
     */
    public RiskResultVo<String> changeCaptcha(String idCard)  {
        RiskResultVo<String> riskResultVo= new RiskResultVo<>();
        try{
            Map<String,String> params= new HashMap<>();
            params.put("idCard",idCard);
            params.put("app_account",appAccount);
            params.put("risk_secret_key",riskSecretKey);
            String response = OkHttpUtils.post(changeCaptchaUrl, params);
            JSONObject jsonObject = JSON.parseObject(response);
            riskResultVo.setCode(jsonObject.getString("code"));
            riskResultVo.setMessage(jsonObject.getString("message"));
            riskResultVo.setData(jsonObject.getString("data"));
        }catch (OKhttpException e) {
            riskResultVo.setMessage("学信网重新获取验证码异常");
            logger.error("学信网重新获取验证码:{}",e);
        }
        return riskResultVo;
    }


    public RiskResultVo<String> tondunLoanEvent(String id_number,String account_name,String account_mobile,String ip_address){
        RiskResultVo<String> resultVo = new RiskResultVo<String>();
        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key", riskSecretKey);
        paramMap.put("id_number",id_number);
        paramMap.put("account_name",account_name);
        paramMap.put("account_mobile",account_mobile);
        paramMap.put("secret_key",tongdunSecretKey);
        paramMap.put("event_id",tongdunLoanEventType);
        paramMap.put("ip_address",ip_address);
        System.out.println("paramMap: "+paramMap);
        try {
            String result=OkHttpUtils.post(loanEventUrl, paramMap);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            resultVo.setCode(code);
            if(code.equals("100")){
                resultVo.setMessage("同盾借款事件成功");
            }else{
                resultVo.setMessage("同盾借款事件失败");
            }
        } catch (OKhttpException e) {
            e.printStackTrace();
            resultVo.setMessage("同盾借款事件异常");
        }
        return resultVo;
    }


    /**
     * 众安省份证图片
     * 2001	姓名和身份不匹配
     * 2003	参数格式不正确
     * 4000	查询失败
     * 5000	系统错误
     * 成功返回的data需要加上  data:image/png;base64,
     * @param name
     * @param idCard
     * @return
     */
    public RiskResultVo<String> zhongAnPicture(String name,String idCard){
        long begin = System.currentTimeMillis();
        LogTrace.info("开始请求众安身份证图片","idCard"+idCard);
        RiskResultVo<String> resultVo = new RiskResultVo<String>();
        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put("app_account", appAccount);
        paramMap.put("risk_secret_key", riskSecretKey);
        paramMap.put("name",name);
        paramMap.put("idCard",idCard);
        try {
            String result=OkHttpUtils.post(identityImageUrl, paramMap);
            LogTrace.info("请求众安身份证图片返回",result);
            JSONObject json = JSON.parseObject(result);
            String code=json.getString("code");
            resultVo.setCode(code);
            if(code.equals("1000")){
                resultVo.setCode(code);
                resultVo.setData(identityImagePrefix+json.getString("data"));
                resultVo.setMessage("众安身份认证图片成功");
            }else{
                resultVo.setCode(code);
                resultVo.setMessage(json.getString("message"));
            }
        } catch (OKhttpException e) {
            e.printStackTrace();
            resultVo.setMessage("众安身份认证图片异常");
        }
        LogTrace.info("请求众安身份证图片结束","elapsed_time", String.valueOf(System.currentTimeMillis()-begin));
        return resultVo;
    }
}
