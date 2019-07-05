package com.xiaochong.loan.background.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaochong.loan.background.component.RiskDataComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.bo.RiskBaseBo;
import com.xiaochong.loan.background.entity.bo.TongdunEducationDataBo;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.entity.vo.yys.YysResultVoData;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.service.base.BaseService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.CheckflowTypeEnum;
import com.xiaochong.loan.background.utils.enums.ContactInfoRelationEnum;
import com.xiaochong.loan.background.utils.enums.OrderFlowStatusEnum;
import com.xiaochong.loan.background.utils.enums.OrderStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service("authService")
public class AuthService extends BaseService {
    private Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Resource(name = "riskDataComponent")
    private RiskDataComponent riskDataComponent;

    @Resource(name = "orderDao")
    private OrderDao orderDao;

    @Resource(name = "merchantDao")
    private MerchantDao merchantDao;

    @Resource(name = "checkflowDao")
    private CheckflowDao checkflowDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Resource(name = "contactInfoDao")
    private ContactInfoDao contactInfoDao;

    @Resource(name = "orderFlowDao")
    private OrderFlowDao orderFlowDao;

    @Resource(name = "bankCardDao")
    private BankCardDao bankCardDao;

    @Resource(name = "authLogDao")
    private AuthLogDao authLogDao;

    @Resource(name = "orderUserInfoDao")
    private OrderUserInfoDao orderUserInfoDao;

    @Value("${risk.education.callback.url}")
    private String educationCallbackUrl;

    @Value("${risk.yys.callback.url}")
    private String yysCallbackUrl;

    @Value("${risk.yys.report.callback.url}")
    private String yysReportCallbackUrl;

    @Value("${risk.zhima.callback.url}")
    private String zhimaCallbackUrl;

    @Value("${risk.zhima.redirect.url}")
    private String zhimaRedirectUrl;

    @Value("${bd.callbackhost}")
    private String bdCallbackhost;

    @Value("${risk.educations.url}")
    private String riskEducationUrl;

    @Value("${risk.app_account}")
    private String appAccount;

    @Value("${risk.secret_key}")
    private String riskSecretKey;

    @Value("${risk.secret_key}")
    private String secretKey;

    @Value("${risk.getToken.url}")
    private String riskTokenUrl;

    private static String token = "";
    /**
     * 学信网认证
     *
     * @param orderToken 订单token
     * @param account    学信网账号
     * @param password   学信网密码
     * @return 接口调用结果
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<String> educationAuth(String orderToken, String account, String password) throws DataDisposeException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(orderToken);
        String name = order.getRealname();
        String idCard = order.getIdCard();
        String mobile = order.getPhone();

        RiskCallbackVo callbackVo = new RiskCallbackVo();
        callbackVo.setCallback_url(educationCallbackUrl);
        callbackVo.setCertNo(idCard);
        callbackVo.setOrderNum(order.getOrderNo());
        RiskResultVo riskResultVo = riskDataComponent.educationAction(name, idCard, mobile, account, password, JSON.toJSONString(callbackVo));
        if (RiskConstantsUtils.EDUCATION_SUCCESS.equals(riskResultVo.getCode())) {
            List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
            orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
            orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
            if(this.updateOrderFlowStatus(
                    order, CheckflowTypeEnum.ACADEMIC,orderFlowStatusEnums,OrderFlowStatusEnum.AUTH)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                return businessVo;
            }
            OrderUserInfo orderUserInfo = new OrderUserInfo();
            orderUserInfo.setOrderNo(order.getOrderNo());
            orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
            if(orderUserInfo!=null){
                orderUserInfo.setEducationAccount(account);
                orderUserInfo.setEducationPassword(password);
                orderUserInfoDao.update(orderUserInfo);
            }else{
                logger.error("更新学信网信息失败！");
                throw new DataDisposeException("更新学信网信息失败！");
            }
        }
        businessVo.setCode(riskResultVo.getCode());
        businessVo.setMessage(riskResultVo.getMessage());
        businessVo.setData(riskResultVo.getMessage());
        return businessVo;
    }

    /**
     * 新 学信网认证
     *
     * @param orderToken 订单token
     * @param account    学信网账号
     * @param password   学信网密码
     * @return 接口调用结果
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<String> educationAuthNew(String orderToken, String account, String password) throws DataDisposeException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(orderToken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.ORDER_TOKEN_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ORDER_TOKEN_ERROR_DESC);
            return businessVo;
        }
        String idCard = order.getIdCard();
        String orderNo = order.getOrderNo();
        RiskResultVo<String> riskResultVo = riskDataComponent.queryEducation(idCard,account,password);
        String code = riskResultVo.getCode();
        String data = riskResultVo.getData();
        if (RiskConstantsUtils.EDUCATION_NEW_SUCCESS.equals(code)) {
            JSONObject jsonObject = JSON.parseObject(data);
            String consistency = jsonObject.getString("consistency");
            if("false".equals(consistency)){
                logger.error("学信验证失败，信息与本人不符，orderNo：{}",orderNo);
                businessVo.setCode(BusinessConstantsUtils.EDUCATION_ACCOUNT_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.EDUCATION_ACCOUNT_ERROR_DESC);
                return businessVo;
            }
            List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
            orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
            orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
            if(this.updateOrderFlowStatus(
                    order,CheckflowTypeEnum.ACADEMIC_NEW,orderFlowStatusEnums,OrderFlowStatusEnum.SUCCESS)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                return businessVo;
            }
            //添加认证日志
            AuthLog authLog = new AuthLog();
            authLog.setType(CheckflowTypeEnum.ACADEMIC_NEW.getType());
            authLog.setIdCard(idCard);
            authLog.setOrderNum(orderNo);
            authLog.setResult(ConstansUtils.SUCCESS);
            authLog.setCreateTime(DateUtils.format(new Date(),DateUtils.ymdhms_format));
            authLog.setErrorMessage(riskResultVo.getMessage());
            authLogDao.insertAuthLog(authLog);
//            if(!BusinessConstantsUtils.SUCCESS_CODE.equals(businessVo.getCode())){
//                logger.info("查无此订单流程：{}", orderNo);
//                return businessVo;
//            }
        }
        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.setOrderNo(orderNo);
        orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
        if(orderUserInfo!=null){
            orderUserInfo.setEducationAccount(account);
            orderUserInfo.setEducationPassword(password);
            orderUserInfoDao.update(orderUserInfo);
        }else{
            logger.error("更新学信网信息失败！");
            throw new DataDisposeException("更新学信网信息失败！");
        }
        businessVo.setCode(code);
        businessVo.setMessage(riskResultVo.getMessage());
        businessVo.setData(riskResultVo.getData());
        return businessVo;
    }



    /**
     * 获取同盾学历信息
     * @param name 姓名
     * @param idCard 身份证
     * @return 返回同盾学历信息
     * @throws OKhttpException 请求异常
     * @throws DataDisposeException 数据处理异常
     */
    public  BusinessVo<TongdunEducationDataBo> getTongdunEducation(String name, String idCard, String orderToken) throws OKhttpException, DataDisposeException {
        BusinessVo<TongdunEducationDataBo> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(orderToken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.ORDER_TOKEN_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.ORDER_TOKEN_ERROR_DESC);
            return businessVo;
        }
        if(StringUtils.isBlank(idCard)){
            idCard = order.getIdCard();
        }
        if(StringUtils.isBlank(name)){
            name = order.getRealname();
        }
        getToken();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("app_account", appAccount);
        paramsMap.put("token", token);
        paramsMap.put("id_card", idCard);
        paramsMap.put("name", name);
        paramsMap.put("app_account", appAccount);
        paramsMap.put("risk_secret_key", secretKey);
        String post = OkHttpUtils.post(riskEducationUrl, paramsMap);
        //logger.info("risk调用 url:{};params:{}",riskEducationUrl,paramsMap);
        RiskBaseBo riskBaseBo = JSON.parseObject(post, RiskBaseBo.class);
        if(ConstansUtils.RISK_TOKEN_ERROR.equals(riskBaseBo.getCode()) || ConstansUtils.RISK_TOKEN_ISNULL.equals(riskBaseBo.getCode())){

            paramsMap.put("token",token);
            post = OkHttpUtils.post(riskEducationUrl, paramsMap);
            riskBaseBo = JSON.parseObject(post, RiskBaseBo.class);
        }
        logger.info("客户学历信息调用结果：{}",post);
        businessVo.setCode(riskBaseBo.getCode());
        businessVo.setMessage(riskBaseBo.getMessage());
        if (ConstansUtils.RISK_RESULT_SUCCESS.equals(riskBaseBo.getCode())){
            List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
            orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
            orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
            if(this.updateOrderFlowStatus(
                    order,CheckflowTypeEnum.ACADEMIC_TONGDUN,orderFlowStatusEnums,OrderFlowStatusEnum.SUCCESS)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                return businessVo;
            }
            businessVo.setData(JSON.parseObject(riskBaseBo.getData(), TongdunEducationDataBo.class));
            return businessVo;
        }else if (ConstansUtils.RISK_RESULT_EMPTY.equals(riskBaseBo.getCode())){
            logger.error("同盾学信验证失败");
            return businessVo;
        }else {
            logger.error("同盾学信验证失败，{}",riskBaseBo.getMessage());
            return businessVo;
        }
    }

    private void getToken() throws DataDisposeException, OKhttpException {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("app_account", appAccount);
        paramsMap.put("secret_key", secretKey);
        String post = OkHttpUtils.post(riskTokenUrl, paramsMap);
        RiskBaseBo riskBaseBo = JSON.parseObject(post, RiskBaseBo.class);
        logger.info("riskBaseBo is :{}",riskBaseBo);
        if (ConstansUtils.RISK_GET_TOKEN_SUCCESS.equals(riskBaseBo.getCode())){
            Map map = JSON.parseObject(riskBaseBo.getData(), Map.class);
            logger.info("map is:{}",map);
            token = map.get("token").toString();
            logger.info("risk token : {}",token);
        }else {
            throw new DataDisposeException(riskBaseBo.getMessage());
        }
    }



    /**
     * 芝麻授权
     *
     * @param orderToken 订单token
     * @param realName   姓名
     * @param idCard     身份证
     * @param phone      手机号
     * @param mid
     * @return 芝麻认证结果
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<String> zhimaAuth(String orderToken, String realName, String idCard, String phone, String mid) throws DataDisposeException {

        BusinessVo<String> businessVo = new BusinessVo<>();

        Order order = orderDao.selectOrderByToken(orderToken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:"+orderToken+";订单不存在！");
            return businessVo;
        }
        if (StringUtils.isBlank(realName) || StringUtils.isBlank(idCard) || StringUtils.isBlank(phone)) {
            realName = order.getRealname();
            idCard = order.getIdCard();
            phone = order.getPhone();
        }

        RiskCallbackVo callbackVo = new RiskCallbackVo();
        callbackVo.setCertNo(idCard);
        callbackVo.setCallback_url(zhimaCallbackUrl);
        String redirectUrl = zhimaRedirectUrl+ "?ordertoken=" + order.getToken() + "&mid=" + order.getMerId()
                + "&stp=0";
        callbackVo.setRedirect_url(redirectUrl);
        callbackVo.setOrderNum(order.getOrderNo());

        RiskResultVo<String> riskResultVo = riskDataComponent.zhimaAction(realName, idCard, phone, JSON.toJSONString(callbackVo));
        businessVo.setCode(riskResultVo.getCode());
        businessVo.setMessage(riskResultVo.getMessage());

        logger.info(riskResultVo.toString());
        if("true".equals(riskResultVo.getMessage())){
            businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
            businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
            List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
            orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
            orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
            if(this.updateOrderFlowStatus(
                    order,CheckflowTypeEnum.ZHIMA,orderFlowStatusEnums,OrderFlowStatusEnum.AUTH)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                return businessVo;
            }

            OrderUserInfo orderUserInfo = new OrderUserInfo();
            orderUserInfo.setOrderNo(order.getOrderNo());
            orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
            if(orderUserInfo!=null){
                orderUserInfo.setRealname(realName);
                orderUserInfo.setIdCard(idCard);
                orderUserInfo.setPhone(phone);
                orderUserInfoDao.update(orderUserInfo);
            }else{
                logger.error("更新芝麻授权信息失败！");
                throw new DataDisposeException("更新芝麻授权信息失败！");
            }

        }
        businessVo.setData(riskResultVo.getData());
        return businessVo;
    }


    /**
     * 身份信息获取
     *
     * @param orderToken orderToken
     * @return Map
     */
    public BusinessVo<Map<String,String>> getZhimaUserMessage(String orderToken) {
        BusinessVo<Map<String,String>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(orderToken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:"+orderToken+";订单不存在！");
            return businessVo;
        }
        User user = userDao.getById(order.getUserId());
        if(user==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("userId:"+order.getUserId()+";用户不存在！");
            return businessVo;
        }
        Map<String,String> returnMap = new HashMap<>();
        returnMap.put("realName",user.getRealname());
        returnMap.put("idCard",user.getIdCard());
        businessVo.setData(returnMap);
        return businessVo;
    }


    /**
     * 运营商认证
     *
     * @param ordertoken ordertoken
     * @param password password
     * @return YysResultVoData
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<YysResultVoData> certificate(String ordertoken, String password) throws DataDisposeException {
        BusinessVo<YysResultVoData> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(ordertoken);
        String id_card = order.getIdCard();
        String user_name = order.getRealname();
        String mobile_no = order.getPhone();
        logger.info("运营商认证入参", id_card, user_name, mobile_no, password);

        RiskCallbackVo riskCallbackVo = new RiskCallbackVo();
        riskCallbackVo.setCallback_url(yysCallbackUrl);
        riskCallbackVo.setCertNo(id_card);
        riskCallbackVo.setOrderNum(order.getOrderNo());
        riskCallbackVo.setYys_report_url(yysReportCallbackUrl);
        try {
            RiskResultVo<YysResultVoData> riskResultVo =
                    riskDataComponent.yysCertificate(
                            id_card, user_name, mobile_no, password, JSON.toJSONString(riskCallbackVo));
            logger.info("yysCertificate返回:",riskResultVo);
            if(RiskConstantsUtils.YYS_CODE_105.equals(riskResultVo.getCode())){
                List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
                orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
                orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
                if(this.updateOrderFlowStatus(
                        order,CheckflowTypeEnum.OPERATOR,orderFlowStatusEnums,OrderFlowStatusEnum.AUTH)){
                    businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                    businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                    return businessVo;
                }

                /*this.updateOrderFlowStatus(
                        order,CheckflowTypeEnum.OPERATOR_REPORT,orderFlowStatusEnums,OrderFlowStatusEnum.AUTH);*/

            }
            businessVo.setCode(riskResultVo.getCode());
            businessVo.setMessage(riskResultVo.getMessage());
            businessVo.setData(riskResultVo.getData());
        } catch (UnsupportedEncodingException | OKhttpException
                | DataDisposeException e) {
            logger.error(e.getMessage(), e);
            logger.error("yys认证提交出现异常", id_card);
            businessVo.setCode(RiskConstantsUtils.YYS_CODE_103);
            businessVo.setMessage(RiskConstantsUtils.YYS_CODE_103_DESC);
        }


        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.setOrderNo(order.getOrderNo());
        orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
        if(orderUserInfo!=null){
            orderUserInfo.setPhonePassword(password);
            orderUserInfoDao.update(orderUserInfo);
        }else{
            logger.error("更新运营商信息失败！");
            throw new DataDisposeException("更新运营商信息失败！");
        }


        logger.info("运营商认证结果", businessVo.toString());
        return businessVo;
    }

    public BusinessVo<YysResultVoData> yysVerifyCode(String ordertoken,
                                                     String task_id, String auth_code,
                                                     String trace_id) {
        BusinessVo<YysResultVoData> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(ordertoken);
        String id_card = order.getIdCard();
        String mobile_no = order.getPhone();
        logger.info("提交验证码入参", id_card, task_id, mobile_no, auth_code, trace_id);
        RiskCallbackVo riskCallbackVo = new RiskCallbackVo();
        riskCallbackVo.setCallback_url(bdCallbackhost + "/user/yys/yys_callback");
        riskCallbackVo.setCertNo(id_card);
        riskCallbackVo.setOrderNum(order.getOrderNo());
        riskCallbackVo.setYys_report_url(yysReportCallbackUrl);
        try {
            RiskResultVo<YysResultVoData> riskResultVo = riskDataComponent.yysVerifyCode(task_id, mobile_no, auth_code, id_card, trace_id, JSON.toJSONString(riskCallbackVo));
            businessVo.setCode(riskResultVo.getCode());
            businessVo.setMessage(riskResultVo.getMessage());
            businessVo.setData(riskResultVo.getData());
        } catch (OKhttpException e) {
            logger.error("yys提交验证码认证异常", e);
            businessVo.setCode(RiskConstantsUtils.YYS_CODE_103);
            businessVo.setMessage(RiskConstantsUtils.YYS_CODE_103_DESC);
        }
        return businessVo;
    }

    /**
     * 提交联系人
     *
     * @param ordertoken
     * @param contactInfosJSON
     * @param homeAddress
     * @param province
     * @param city
     * @param area
     * @param permanentAddress @return
     * @throws DataDisposeException
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<String> submitContact(String ordertoken, String contactInfosJSON,
                                            String homeAddress, String province, String city,
                                            String area, String permanentAddress) throws DataDisposeException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(ordertoken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:"+ordertoken+";订单不存在！");
            return businessVo;
        }

        JSONArray padContactUserArray = new JSONArray();
        List<ContactInfo> contactInfos = JSONArray.parseArray(contactInfosJSON, ContactInfo.class);
        for (ContactInfo contactInfo:contactInfos) {
            contactInfo.setOrderNo(order.getOrderNo());
            contactInfo.setOrderToken(order.getToken());
            contactInfo.setMerchId(order.getMerId());
            contactInfo.setIdCard(order.getIdCard());
            if(!ContactInfoRelationEnum.NO_TYPE.getType().equals(contactInfo.getRelation())){
                ContactInfoRelationEnum contactInfoRelation =
                        ContactInfoRelationEnum.getContactInfoRelationByType(contactInfo.getRelation());
                contactInfo.setRelationName(
                        contactInfoRelation!=null?contactInfoRelation.getName():null);
            }

            if(contactInfoDao.insertOrUpdate(contactInfo)==0){
                logger.error("更新联系人信息失败！");
                throw new DataDisposeException("更新联系人信息失败！");
            }
            JSONObject padContactUserObject = riskDataComponent.padContactUser(order.getIdCard(),
                    contactInfo.getRelation(),contactInfo.getRealName(), contactInfo.getPhone(),
                    ( ContactInfoRelationEnum.TYPE_FATHER.getType().equals(contactInfo.getRelation())
                    ||ContactInfoRelationEnum.TYPE_MOTHER.getType().equals(contactInfo.getRelation()) )
                    ?homeAddress:""
            );
            padContactUserArray.add(padContactUserObject);
        }
        // 上传联系人信息到risk
        RiskResultVo<String> resultVo = riskDataComponent.subUserConatct(order.getIdCard(), padContactUserArray);
        logger.info(resultVo.toString());
        if(RiskConstantsUtils.SUBMIT_CONTACT_CODE_SUCCESS.equals(resultVo.getCode())){
            List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
            orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
            orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
            if(this.updateOrderFlowStatus(
                    order,CheckflowTypeEnum.CONTACT,orderFlowStatusEnums,OrderFlowStatusEnum.SUCCESS)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                return businessVo;
            }

            OrderUserInfo orderUserInfo = new OrderUserInfo();
            orderUserInfo.setOrderNo(order.getOrderNo());
            orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
            if(orderUserInfo!=null){
                orderUserInfo.setHomeAddress(homeAddress);
                orderUserInfo.setHomeAddressProvince(province);
                orderUserInfo.setHomeAddressCity(city);
                orderUserInfo.setHomeAddressArea(area);
                orderUserInfo.setPermanentAddress(permanentAddress);
                orderUserInfoDao.update(orderUserInfo);
            }else{
                logger.error("更新运营商信息失败！");
                throw new DataDisposeException("更新运营商信息失败！");
            }


        }

        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage("联系信息提交成功");
        return businessVo;
    }



    public BusinessVo<List<ContactInfoWebappVo>> getConatctInfo(String ordertoken) {
        BusinessVo<List<ContactInfoWebappVo>> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setOrderToken(ordertoken);
        List<ContactInfo> contactInfos = contactInfoDao.listByContactInfo(contactInfo);
        if(contactInfos==null || contactInfos.size()==0){
            return businessVo;
        }
        List<ContactInfoWebappVo> returnVoList = new ArrayList<>();
        for (ContactInfo info:contactInfos) {
            ContactInfoWebappVo vo = new ContactInfoWebappVo();
            vo.setContactInfo(info);
            returnVoList.add(vo);
        }
        businessVo.setData(returnVoList);
        return businessVo;
    }


    /**
     * 提交银行卡信息
     *
     * @param ordertoken ordertoken
     * @param bankName   bankName
     * @param bankCard   bankCard
     * @param bankPhone  bankPhone
     * @return BusinessVo<RiskResultVo>
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<String> submitBankCard(String ordertoken, String bankName, String bankCard, String bankPhone) throws DataDisposeException {
        BusinessVo<String> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(ordertoken);
        RiskResultVo<String> riskResultVo = riskDataComponent.unionPayAuthAction(order.getRealname(),
                order.getIdCard(), bankPhone, bankCard);
        logger.info(riskResultVo.toString());
        businessVo.setCode(riskResultVo.getCode());
        businessVo.setMessage(riskResultVo.getMessage());
        businessVo.setData(riskResultVo.getData());
        if (!RiskConstantsUtils.UNIONPAY_SUCCESS.equals(riskResultVo.getCode())) {
            return businessVo;
        }
        BankCard loanBankCard = new BankCard();
        loanBankCard.setIdCard(order.getIdCard());
        loanBankCard = bankCardDao.getByBankCard(loanBankCard);
        boolean flag = false;
        if (loanBankCard == null) {
            flag = true;
            loanBankCard = new BankCard();
            loanBankCard.setIdCard(order.getIdCard());
        }
        loanBankCard.setBankName(bankName);
        loanBankCard.setBankCard(bankCard);
        loanBankCard.setBankPhone(bankPhone);
        if (flag) {
            flag = bankCardDao.insert(loanBankCard) > 0;
        } else {
            flag = bankCardDao.update(loanBankCard) > 0;
        }
        if (!flag) {
            logger.error("更新绑卡信息失败！");
            throw new DataDisposeException("更新绑卡信息失败！");
        }
        return businessVo;
    }

    /**
     * 认证信息获取
     *
     * @param orderToken orderToken
     * @return OrderAuthWebappVo
     */
    public BusinessVo<OrderAuthWebappVo> orderAuth(String orderToken) {
        BusinessVo<OrderAuthWebappVo> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(orderToken);
        if (order == null) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:" + orderToken + ";订单不存在！");
            return businessVo;
        }


        Merchantinfo merchantinfo = merchantDao.findMerchantinfoByToid(order.getMerId());
        if (merchantinfo == null) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("公司不存在！");
            return businessVo;
        }
        OrderAuthWebappVo returnVo = new OrderAuthWebappVo();
        returnVo.setOrderStatus(order.getStatus());
        returnVo.setMerchantName(merchantinfo.getMerchantName());

        OrderFlow searchOrderFlow = new OrderFlow();
        searchOrderFlow.setOrderNo(order.getOrderNo());
        List<OrderFlow> orderFlows = orderFlowDao.listByOrderFlow(searchOrderFlow);
        if (orderFlows == null || orderFlows.size() == 0) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:" + orderToken + ";orderNo" + order.getOrderNo() +
                    "订单流程不存在！");
            return businessVo;
        }
        List<OrderFlowWebappVo> orderFlowVos = new ArrayList<>();
        for (OrderFlow orderFlow : orderFlows) {
            Checkflow checkflow = new Checkflow();
            checkflow.setFlowNo(orderFlow.getFlowNo());
            checkflow = checkflowDao.getByCheckflow(checkflow);
            if (checkflow == null) {
                continue;
            }
            OrderFlowWebappVo orderFlowVo = new OrderFlowWebappVo();
            orderFlowVo.setCheckflow(checkflow);
            orderFlowVo.setOrderFlow(orderFlow);
            orderFlowVos.add(orderFlowVo);
        }
        returnVo.setOrderFlowWebappVos(orderFlowVos);
        businessVo.setData(returnVo);

        return businessVo;
    }

    @Autowired
    private AccountRecordDao accountRecordDao;

    /**
     * 用户授权
     *
     * @param orderToken orderToken
     * @param status
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<Boolean> userAuth(String orderToken, boolean status,
                                        String latitude,String longitude,String clientIp) throws DataDisposeException {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(orderToken);

        if (order == null || !OrderStatusEnum.UNAUTH.getType().equals(order.getStatus())) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("userAuth订单不存在或状态有误！orderToken:{};order:{};",orderToken,order);
            return businessVo;
        }
        if(OrderStatusEnum.REJECT.getType().equals(order.getStatus())){
            businessVo.setCode(BusinessConstantsUtils.AUTH_REJECT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.AUTH_REJECT_DESC);
            logger.info("认证流程已拒绝");
            return businessVo;
        }
        order.setStatus(status?OrderStatusEnum.AUTH.getType():OrderStatusEnum.REJECT.getType());
        if (orderDao.update(order) != 1) {
            businessVo.setCode(BusinessConstantsUtils.UPDATE_FAILED_CODE);
            businessVo.setMessage(BusinessConstantsUtils.UPDATE_FAILED_DESC);
            return businessVo;
        }
        AccountRecord accountRecord = new AccountRecord();
        accountRecord.setOrderNo(order.getOrderNo());
        accountRecord.setOrderStatus(order.getStatus());
        accountRecordDao.updateAccountRecordByOrderNo(accountRecord);
        if(!status){
            return businessVo;
        }

        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.setOrderNo(order.getOrderNo());
        orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
        if(orderUserInfo!=null){
            orderUserInfo.setLatitude(latitude);
            orderUserInfo.setLongitude(longitude);
            orderUserInfo.setClientIp(!"127.0.0.1".equals(clientIp)?clientIp:"");
            orderUserInfo.setAuthStartTime(new Date());
            orderUserInfoDao.update(orderUserInfo);
        }else{
            logger.error("更新授权信息失败！");
            throw new DataDisposeException("更新授权信息失败！");
        }

        businessVo.setData(true);
        return businessVo;

    }

    @Transactional(rollbackFor = Exception.class)
    public BusinessVo<Boolean> userEndAuth(String orderToken,
                                        String latitude,String longitude) throws DataDisposeException {
        BusinessVo<Boolean> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(orderToken);

        if (order == null) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("userEndAuth订单不存在！orderToken:{};",orderToken);
            return businessVo;
        }
        if(OrderStatusEnum.REJECT.getType().equals(order.getStatus())){
            businessVo.setCode(BusinessConstantsUtils.AUTH_REJECT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.AUTH_REJECT_DESC);
            logger.info("userEndAuth订单认证流程已拒绝");
            return businessVo;
        }

        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.setOrderNo(order.getOrderNo());
        orderUserInfo = orderUserInfoDao.getByOrderUserInfo(orderUserInfo);
        if(orderUserInfo==null){
            businessVo.setCode(BusinessConstantsUtils.AUTH_REJECT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.AUTH_REJECT_DESC);
            logger.info("userEndAuth orderUserInfo为空order:{}",order);
            return businessVo;
        }
        orderUserInfo.setEndLatitude(latitude);
        orderUserInfo.setEndLongitude(longitude);
        orderUserInfo.setAuthEndTime(new Date());
        orderUserInfoDao.update(orderUserInfo);

        businessVo.setData(true);
        return businessVo;

    }

    /**
     * 获取银行卡信息
     *
     * @param ordertoken ordertoken
     * @return BankCard
     */
    public BusinessVo<BankCard> getBankCard(String ordertoken) {
        BusinessVo<BankCard> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(ordertoken);
        if (order == null) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:" + ordertoken + ";订单不存在！");
            return businessVo;
        }
        BankCard bankCard = new BankCard();
        bankCard.setIdCard(order.getIdCard());
        businessVo.setData(bankCardDao.getByBankCard(bankCard));
        return businessVo;
    }

    /**
     * 提交订单接口
     *
     * @param orderToken 订单token
     * @param latitude   经度
     * @param longitude  纬度
     * @return 操作结果
     */
    public BusinessVo<String> submitRiskOrder(String orderToken, String latitude, String longitude) {

        BusinessVo<String> businessVo = new BusinessVo<>();

        Order order = orderDao.selectOrderByToken(orderToken);
        String idCard = order.getIdCard();
        String accountName = order.getRealname();
        String phone = order.getPhone();

        List<OrderFlow> orderFlows = orderFlowDao.getByOrderNo(order.getOrderNo());

        boolean checkAuthRsult = true;
        for (OrderFlow orderFlow : orderFlows) {
            //是否有未完成的认证项
            if (!OrderFlowStatusEnum.SUCCESS.getType().equals(orderFlow.getStatus())) {
                checkAuthRsult = false;
            }
        }

        if (checkAuthRsult) {
            //todo 获取ip和设置回调地址
            String clientIp = "";
            String callbackUrl = "";

            RiskResultVo<String> loanRiskResult = riskDataComponent.tongdunLoanEvent(idCard, accountName, phone, clientIp);
            if (!loanRiskResult.getCode().equals("100")) {
                logger.error("同盾贷款事件错误", new Exception());
            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(order.getOrderNo());
            orderInfo.setCreatedAt(DateUtils.getCurrentDateStr());
            orderInfo.setGisLatitude(latitude);
            orderInfo.setGisLongitude(longitude);
            RiskResultVo<String> submitOrderRiskResultVo = riskDataComponent.submitOrder(idCard, clientIp, callbackUrl, JSON.toJSONString(orderInfo));
            if (submitOrderRiskResultVo.getCode().equals("100")) {
                businessVo.setCode(ConstansUtils.SUCCESS);
                businessVo.setMessage("订单提交成功");
            } else {
                businessVo.setCode(ConstansUtils.ERROR);
                businessVo.setMessage("订单提交失败");
            }
        } else {
            businessVo.setCode(ConstansUtils.ERROR);
            businessVo.setMessage("验证未完全通过，请稍后再试");
        }

        return businessVo;
    }

    public BusinessVo<AuthLogWebappVo> getZhimaCallbackMessage(String orderToken) {
        BusinessVo<AuthLogWebappVo> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(orderToken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:"+orderToken+";订单不存在！");
            return businessVo;
        }
        AuthLog authLog = new AuthLog();
        authLog.setType(RiskConstantsUtils.ZM_TYPE);
        authLog.setIdCard(order.getIdCard());
        authLog.setOrderNum(order.getOrderNo());
        authLog = authLogDao.getByAuthLog(authLog);
        if(authLog==null){
            return businessVo;
        }
        AuthLogWebappVo authLogWebappVo = new AuthLogWebappVo();
        authLogWebappVo.setAuthLog(authLog);
        businessVo.setData(authLogWebappVo);
        return businessVo;
    }

    /**
     * 订单信息获取
     *
     * @param orderToken orderToken
     * @return OrderWebappVo
     */
    public BusinessVo<OrderWebappVo> getOrderWebappVo(String orderToken) {
        BusinessVo<OrderWebappVo> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        Order order = orderDao.selectOrderByToken(orderToken);
        if(order==null){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:"+orderToken+";订单不存在！");
            return businessVo;
        }
        OrderWebappVo orderWebappVo = new OrderWebappVo();
        orderWebappVo.setOrderNo(order.getOrderNo());
        orderWebappVo.setRealname(order.getRealname());
        orderWebappVo.setPhone(order.getPhone());
        orderWebappVo.setStatus(order.getStatus());
        orderWebappVo.setCreatetime(DateUtils.format(order.getCreatetime(),DateUtils.ymdhms_format));
        businessVo.setData(orderWebappVo);
        return businessVo;
    }

    /**
     * 获取认证跳转
     *
     * @param orderToken orderToken
     * @return CheckflowVo
     */
    public BusinessVo<CheckflowVo> findNextCheckFlow(String orderToken) {
        BusinessVo<CheckflowVo> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);

        Order order = orderDao.selectOrderByToken(orderToken);
        if (order == null) {
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:{};订单不存在！",orderToken);
            return businessVo;
        }
        if(!OrderStatusEnum.AUTH.getType().equals(order.getStatus())){
            if(OrderStatusEnum.REJECT.getType().equals(order.getStatus())){
                businessVo.setCode(BusinessConstantsUtils.AUTH_REJECT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.AUTH_REJECT_DESC);
                logger.info("认证流程已拒绝");
            }
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:{};order:{};订单状态有误！",orderToken,order);
            return businessVo;
        }


        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setOrderToken(orderToken);
        List<OrderFlow> orderFlows = orderFlowDao.listByOrderFlow(orderFlow);
        if(orderFlows==null || orderFlows.size()==0){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            logger.warn("orderToken:"+orderToken+";订单认证(orderFlow)流程信息不存在！");
            return businessVo;
        }
        boolean authFlag = false;
        for (OrderFlow flow:orderFlows) {
            Checkflow checkflow = new Checkflow();
            checkflow.setFlowNo(flow.getFlowNo());
            checkflow = checkflowDao.getByCheckflow(checkflow);
            if(checkflow==null){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                logger.warn("FlowNo:"+orderFlows.get(0).getFlowNo()+";认证信息(checkflow)不存在！");
                return businessVo;
            }
            if( OrderFlowStatusEnum.AUTH.getType().equals(flow.getStatus()) ){
                authFlag = true;
                continue;
            }
            if(
                    (CheckflowTypeEnum.ZHIMA.getType().equals(checkflow.getType()) &&
                            !OrderFlowStatusEnum.SUCCESS.getType().equals(flow.getStatus()))||
                    (OrderFlowStatusEnum.UNAUTH.getType().equals(flow.getStatus())||
                            OrderFlowStatusEnum.FAIL.getType().equals(flow.getStatus()) )
                    ){
                CheckflowVo checkflowVo = new CheckflowVo();
                checkflowVo.setCheckflow(checkflow);
                businessVo.setData(checkflowVo);
                return businessVo;
            }

        }
        if(authFlag){
            businessVo.setCode(BusinessConstantsUtils.AUTH_WAIT_CALLBACK_CODE);
            businessVo.setMessage(BusinessConstantsUtils.AUTH_WAIT_CALLBACK_DESC);
            logger.info("认证流程完成待返回");
        }
        businessVo.setCode(BusinessConstantsUtils.AUTH_DONE_CODE);
        businessVo.setMessage(BusinessConstantsUtils.AUTH_DONE_DESC);
        logger.info("认证流程已完成");
        return businessVo;
    }

    private OrderFlow getOrderFlow(String orderNo, CheckflowTypeEnum academic) {
        Checkflow checkflow = new Checkflow();
        checkflow.setType(academic.getType());
        checkflow = checkflowDao.getByCheckflow(checkflow);
        if(checkflow==null){return null;}
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setOrderNo(orderNo);
        orderFlow.setFlowNo(checkflow.getFlowNo());
        return orderFlowDao.getByOrderFlow(orderFlow);
    }

    private boolean updateOrderFlowStatus(Order order, CheckflowTypeEnum academic, List<OrderFlowStatusEnum> flowStatusEnums, OrderFlowStatusEnum updateFlowStatusEnum) {
        BusinessVo businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        OrderFlow orderFlow = this.getOrderFlow(
                order.getOrderNo(),academic);
        if (orderFlow == null) {
            logger.error("orderToken:" + order.getToken() + ";orderNo" + order.getOrderNo() +
                    "订单流程不存在！");
            return false;
        }
        boolean isEqualsFlag = false;
        for (OrderFlowStatusEnum flowStatusEnum:flowStatusEnums) {
            if(flowStatusEnum.getType().equals(orderFlow.getStatus())){
                isEqualsFlag = true;
                break;
            }
        }
        if(!isEqualsFlag){
            logger.error("orderToken:" + order.getToken() + ";orderNo" + order.getOrderNo() +
                    "订单流程状态有误！");
            return false;
        }
        orderFlow.setStatus(updateFlowStatusEnum.getType());
        if (orderFlowDao.update(orderFlow) != 1) {
            logger.error("订单流程更新失败！");
            return false;
        }
        return false;
    }

    /**
     * 新学信通过验证码获取
     * @param traceId
     * @param verificationCode
     * @return
     */
    public BusinessVo<String> eductionAuthByVerificationCode(String orderToken,String traceId, String verificationCode) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(orderToken);
        String idCard = order.getIdCard();
        AuthLog authLog = new AuthLog();
        authLog.setType(CheckflowTypeEnum.ACADEMIC_NEW.getType());
        authLog.setIdCard(idCard);
        String orderNo = order.getOrderNo();
        authLog.setOrderNum(orderNo);
        AuthLog byAuthLog = authLogDao.getByAuthLog(authLog);
        logger.info("新学信验证码提交接口:{}",byAuthLog);
        //防止重复验证

        if(byAuthLog!=null&&ConstansUtils.SUCCESS.equals(byAuthLog.getResult())){
            businessVo.setCode(BusinessConstantsUtils.FLOW_ALREADY_PASS_ERROR_CODE);
            businessVo.setMessage(BusinessConstantsUtils.FLOW_ALREADY_PASS_ERROR_DESC);
            return businessVo;
        }
        RiskResultVo<String> riskResultVo = riskDataComponent.queryWithCaptcha(idCard,verificationCode,traceId);
        String code = riskResultVo.getCode();
        authLog.setErrorMessage(riskResultVo.getMessage());
        String data = riskResultVo.getData();
        if (RiskConstantsUtils.EDUCATION_NEW_SUCCESS.equals(code)) {
            JSONObject jsonObject = JSON.parseObject(data);
            String consistency = jsonObject.getString("consistency");
            if("false".equals(consistency)){
                logger.error("学信验证失败，信息与本人不符，orderNo：{}",orderNo);
                businessVo.setCode(BusinessConstantsUtils.EDUCATION_ACCOUNT_ERROR_CODE);
                businessVo.setMessage(BusinessConstantsUtils.EDUCATION_ACCOUNT_ERROR_DESC);
                return businessVo;
            }
            authLog.setResult(ConstansUtils.SUCCESS);
            List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
            orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
            orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
            if(this.updateOrderFlowStatus(
                    order,CheckflowTypeEnum.ACADEMIC_NEW,orderFlowStatusEnums,OrderFlowStatusEnum.SUCCESS)){
                businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
                businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
                return businessVo;
            }
        }else {
            authLog.setResult(ConstansUtils.ERROR);
        }
        if(byAuthLog!=null&&ConstansUtils.ERROR.equals(byAuthLog.getResult())){
            authLog.setId(byAuthLog.getId());
            authLog.setUpdateTime(DateUtils.format(new Date(),DateUtils.ymdhms_format));
            authLogDao.updateSelective(authLog);
        }else {
            authLog.setCreateTime(DateUtils.format(new Date(),DateUtils.ymdhms_format));
            authLogDao.insertAuthLog(authLog);
        }

        businessVo.setCode(code);
        businessVo.setMessage(riskResultVo.getMessage());
        businessVo.setData(data);
        return  businessVo;
    }

    /**
     * 新学信网重新获取验证码
     * @param orderToken
     * @return
     */
    public BusinessVo<String> eductionUpdateVerificationCode(String orderToken) {
        BusinessVo<String> businessVo = new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(orderToken);
        String idCard = order.getIdCard();
        RiskResultVo<String> riskResultVo = riskDataComponent.changeCaptcha(idCard);
        String code = riskResultVo.getCode();
        businessVo.setCode(code);
        businessVo.setMessage(riskResultVo.getMessage());
        businessVo.setData(riskResultVo.getData());
        return  businessVo;
    }


    /**
     * 绑卡信息
     * @param bank bank
     * @param ordertoken ordertoken
     * @return String
     */
    public BusinessVo<String> insertBankCard(BankCard bank, String ordertoken) {
        BusinessVo<String> businessVo= new BusinessVo<>();
        Order order = orderDao.selectOrderByToken(ordertoken);
        BankCard bankCardForSearch= new BankCard();
        String idCard = order.getIdCard();
        String orderNo = order.getOrderNo();
        bankCardForSearch.setIdCard(idCard);
        bankCardForSearch.setOrderNo(orderNo);
        List<BankCard> bankCardList = bankCardDao.selectByBankCard(bankCardForSearch);
        bank.setIdCard(idCard);
        bank.setOrderNo(orderNo);
        Date date = new Date();
        if(bankCardList!=null&&bankCardList.size()!=0){
            bank.setId(bankCardList.get(0).getId());
            bank.setUpdatetime(date);
            bankCardDao.update(bank);
            businessVo.setData("绑卡信息更新成功");
        }else {
            bank.setCreatetime(date);
            bankCardDao.insert(bank);
            businessVo.setData("绑卡信息添加成功");
        }
        List<OrderFlowStatusEnum> orderFlowStatusEnums = new ArrayList<>();
        orderFlowStatusEnums.add(OrderFlowStatusEnum.UNAUTH);
        orderFlowStatusEnums.add(OrderFlowStatusEnum.FAIL);
        if(this.updateOrderFlowStatus(
                order,CheckflowTypeEnum.BANK_CARD,orderFlowStatusEnums,OrderFlowStatusEnum.SUCCESS)){
            businessVo.setCode(BusinessConstantsUtils.INCORRECT_DATA_INPUT_CODE);
            businessVo.setMessage(BusinessConstantsUtils.INCORRECT_DATA_INPUT_DESC);
            return businessVo;
        }
        AuthLog authLog = new AuthLog();
        authLog.setType(CheckflowTypeEnum.BANK_CARD.getType());
        authLog.setIdCard(idCard);
        authLog.setOrderNum(orderNo);
        AuthLog byAuthLog = authLogDao.getByAuthLog(authLog);
        authLog.setResult(ConstansUtils.SUCCESS);
        if(byAuthLog!=null&&byAuthLog.getId()!=null){
            authLog.setId(byAuthLog.getId());
            authLog.setUpdateTime(DateUtils.format(date,DateUtils.ymdhms_format));
            authLogDao.updateSelective(authLog);
        }else {
            authLog.setCreateTime(DateUtils.format(date,DateUtils.ymdhms_format));
            authLogDao.insertAuthLog(authLog);
        }
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setMessage(BusinessConstantsUtils.SUCCESS_DESC);
        return  businessVo;
    }

}
