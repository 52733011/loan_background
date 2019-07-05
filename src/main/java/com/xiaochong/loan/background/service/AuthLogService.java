package com.xiaochong.loan.background.service;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import com.alibaba.fastjson.JSONObject;
import com.xiaochong.loan.background.component.SMSComponent;
import com.xiaochong.loan.background.dao.*;
import com.xiaochong.loan.background.entity.po.*;
import com.xiaochong.loan.background.entity.vo.AkkaRiskOrderParamsVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.akkaLog.AkkaRouterUtils;
import com.xiaochong.loan.background.utils.akkaLog.Process;
import com.xiaochong.loan.background.utils.enums.CheckflowTypeEnum;
import com.xiaochong.loan.background.utils.enums.OrderFlowStatusEnum;
import com.xiaochong.loan.background.utils.enums.OrderStatusEnum;
import com.xiaochong.loan.background.utils.enums.TransactTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Service("authLogService")
public class AuthLogService {
    private Logger logger = LoggerFactory.getLogger(AuthLogService.class);

    @Resource(name = "authLogDao")
    private AuthLogDao authLogDao;

    @Resource(name = "orderDao")
    private OrderDao orderDao;

    @Resource(name = "orderFlowDao")
    private OrderFlowDao orderFlowDao;

    @Resource(name = "checkflowDao")
    private CheckflowDao checkflowDao;

    @Resource(name = "smsComponent")
    private SMSComponent smsComponent;

    @Value("${bd.callbackhost}")
    private String bdCallbackhost;

    @Autowired
    private AccountRecordDao accountRecordDao;

    /**
     * 运营商回调
     *
     * @param error_message 错误信息
     * @param success       是否成功
     * @param error_code    错误编码
     * @param callback_set  回调参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void callBackOption(String error_message, String success, String error_code, String callback_set, String authType) throws Exception {
        logger.info("收到回调请求error_message【{}】，success【{}】，error_code【{}】，callback_set【{}】，authType【{}】",
                error_message, success, error_code, callback_set, authType);
        Map<String,Object> logMap = new HashMap<>();
        logMap.put("error_message",error_message);
        logMap.put("success",success);
        logMap.put("error_code",error_code);
        logMap.put("callback_set",callback_set);
        logMap.put("authType",authType);
        logMap.put("log",LogTraceUtils.logReplace("收到回调请求error_message【{}】，success【{}】，error_code【{}】，callback_set【{}】，authType【{}】",
                error_message, success, error_code, callback_set, authType ));
        LogTraceUtils.info("callback",logMap);
        if (callback_set.contains("%")) {
            callback_set = URLDecoder.decode(callback_set, "UTF-8");
        }

        String idCard = JsonUtils.getNodeValue(callback_set, "certNo");
        String orderNum = JsonUtils.getNodeValue(callback_set, "orderNum");
        AuthLog authLog = new AuthLog();
        authLog.setType(authType);
        authLog.setIdCard(idCard);
        authLog.setOrderNum(orderNum);
        authLog.setErrorMessage(error_message);
        Order order = new Order();
        order.setOrderNo(orderNum);
        order = orderDao.getByOrder(order);
        if(order==null){
            logger.error("订单不存在orderNum【{}】",orderNum);
            logMap = new HashMap<>();
            logMap.put("orderNum",orderNum);
            logMap.put("log",LogTraceUtils.logReplace("订单不存在orderNum【{}】", orderNum ));
            LogTraceUtils.error("callback",logMap,new DataDisposeException("订单不存在!"));
            throw new DataDisposeException("订单不存在!");
        }
        CheckflowTypeEnum checkflowTypeEnum = null;
        CheckflowTypeEnum[] checkflowTypeEnums = CheckflowTypeEnum.values();
        for (CheckflowTypeEnum e:checkflowTypeEnums) {
            if(e.getType().equals(authType)){
                checkflowTypeEnum=e;
            }
        }
        if (error_code.equals("SUCCESS") && success.equals("true")) {
            // 认证通过
            authLog.setResult(ConstansUtils.SUCCESS);

            if(!this.updateOrderFlowStatus(
                    order,checkflowTypeEnum, OrderFlowStatusEnum.AUTH,OrderFlowStatusEnum.SUCCESS)){
                throw new DataDisposeException("OrderFlow更新失败！");
            }
        } else {
            // 认证失败
            authLog.setResult(ConstansUtils.ERROR);

            if(!this.updateOrderFlowStatus(
                    order,checkflowTypeEnum,OrderFlowStatusEnum.AUTH,OrderFlowStatusEnum.FAIL)){
                throw new DataDisposeException("OrderFlow更新失败！");
            }

            // 生成url
            String auth_url = bdCallbackhost + "h5/authorization.html"
                    + "?ordertoken=" + order.getOrderNo() + "&mid=" + order.getMerId()
                    + "&stp=0";
            smsComponent.sendSms(checkflowTypeEnum.getName()+"认证失败！请重新认证：" +
                    auth_url + "", order.getPhone());
        }
        if(this.insertOrUpdateAuthLog(authLog)!=1){
            throw new DataDisposeException("authLog更新失败！");
        }
    }


    public void yysReportCallBack(String yys_report_url,String orderNum,String yys_report_success,String idCard, String authType) throws DataDisposeException, OKhttpException {
        logger.info("yys_report_url【{}】，orderNum【{}】，yys_report_success【{}】，id_card 【{}】",
                yys_report_url, orderNum, yys_report_success, idCard);
        Map<String,Object> logMap = new HashMap<>();
        logMap.put("yys_report_url",yys_report_url);
        logMap.put("orderNum",orderNum);
        logMap.put("yys_report_success",yys_report_success);
        logMap.put("idCard",idCard);
        logMap.put("authType",authType);
        logMap.put("log",LogTraceUtils.logReplace("收到回调请求yys_report_url【{}】，orderNum【{}】，yys_report_success【{}】，idCard【{}】，authType【{}】",
                yys_report_url, orderNum, yys_report_success, idCard, authType ));
        LogTraceUtils.info("callback",logMap);
        AuthLog authLog = new AuthLog();
        authLog.setType(authType);
        authLog.setIdCard(idCard);
        authLog.setOrderNum(orderNum);
        authLog.setErrorMessage(yys_report_success);
        Order order = new Order();
        order.setOrderNo(orderNum);
        order = orderDao.getByOrder(order);
        if(order==null){
            logger.error("订单不存在orderNum【{}】",orderNum);
            logMap = new HashMap<>();
            logMap.put("orderNum",orderNum);
            logMap.put("log",LogTraceUtils.logReplace("订单不存在orderNum【{}】", orderNum ));
            LogTraceUtils.error("callback",logMap,new DataDisposeException("订单不存在!"));
            throw new DataDisposeException("订单不存在!");
        }
        CheckflowTypeEnum checkflowTypeEnum = null;
        CheckflowTypeEnum[] checkflowTypeEnums = CheckflowTypeEnum.values();
        for (CheckflowTypeEnum e:checkflowTypeEnums) {
            if(e.getType().equals(authType)){
                checkflowTypeEnum=e;
            }
        }
        if (yys_report_success.equals("true")) {
            // 认证通过
            authLog.setResult(ConstansUtils.SUCCESS);

            if(!this.updateOrderFlowStatus(
                    order,checkflowTypeEnum,null,OrderFlowStatusEnum.SUCCESS)){
                throw new DataDisposeException("OrderFlow更新失败！");
            }
        } else {
            // 认证失败
            authLog.setResult(ConstansUtils.ERROR);

            if(!this.updateOrderFlowStatus(
                    order,checkflowTypeEnum,null,OrderFlowStatusEnum.FAIL)){
                throw new DataDisposeException("OrderFlow更新失败！");
            }

            // 生成url
            String auth_url = bdCallbackhost + "h5/authorization.html"
                    + "?ordertoken=" + order.getOrderNo() + "&mid=" + order.getMerId()
                    + "&stp=0";
            smsComponent.sendSms(checkflowTypeEnum.getName()+"认证失败！请重新认证：" +
                    auth_url + "", order.getPhone());
        }
        if(this.insertOrUpdateAuthLog(authLog)!=1){
            throw new DataDisposeException("authLog更新失败！");
        }
    }

    /**
     * risk订单回调操作
     *
     * @param callback_data 回调内容
     * @param success       是否成功
     * @param error_code    错误编码
     */
    public void riskOrderCallback(String callback_data, String success, String error_code,String callback_set) {
        try {
            Map callBackData = JSONObject.parseObject(callback_data, Map.class);
            String orderno = callBackData.get("orderno").toString();
            String status = callBackData.get("status").toString();
            String check_result = callBackData.get("check_result").toString();
            // 进入人审
            Order order = orderDao.selectOrderByOrdeNum(orderno);
            if(order==null){
                logger.error("订单不存在！orderno:{}",orderno);
                Map<String,Object> logMap = new HashMap<>();
                logMap.put("orderno",orderno);
                logMap.put("log",LogTraceUtils.logReplace("订单不存在orderno【{}】", orderno ));
                LogTraceUtils.error("callback",logMap,new DataDisposeException("订单不存在!"));
                throw new DataDisposeException("订单不存在！");
            }
            if (status.equals("review")) {
                order.setRiskStatus(RiskConstantsUtils.AUTO_RISK_REVIEW);
            } else if (status.equals("reject")) {
                order.setRiskStatus(RiskConstantsUtils.AUTO_RISK_REJECT);
            }
            order.setStatus(OrderStatusEnum.FINISH.getType());
            order.setRiskCheckResult(check_result);

            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setOrderNo(order.getOrderNo());
            accountRecord.setOrderStatus(order.getStatus());
            accountRecord.setTransactType(TransactTypeEnum.EXPEND.getType());
            accountRecordDao.updateAccountRecordByOrderNo(accountRecord);

            orderDao.update(order);

            //actor报告解析入库
            AkkaRiskOrderParamsVo akkaRiskOrderParamsVo = new AkkaRiskOrderParamsVo();
            akkaRiskOrderParamsVo.setIdCard(order.getIdCard());
            akkaRiskOrderParamsVo.setOrderNum(orderno);
//            akkaComponent.process(new ReportComponent(),akkaRiskOrderParamsVo);
            Process reportComponent = AkkaRouterUtils.getProcess("reportComponent");
            reportComponent.process(akkaRiskOrderParamsVo, new OnSuccess() {
                @Override
                public void onSuccess(Object o) throws Throwable {
//                    LogTraceUtils.info("report_success","orderno",orderno);
                    logger.info("报告异步完成",akkaRiskOrderParamsVo);
                    LogTraceUtils.info("callback","报告异步完成akkaRiskOrderParamsVo="+akkaRiskOrderParamsVo);
                }
            }, new OnFailure() {
                @Override
                public void onFailure(Throwable throwable) throws Throwable {
//                    LogTraceUtils.info("report_fail","orderno",orderno);
                    logger.error("报告异步失败！",akkaRiskOrderParamsVo);
                    LogTraceUtils.error("callback","报告异步失败!akkaRiskOrderParamsVo="+akkaRiskOrderParamsVo
                            ,new DataDisposeException("报告异步失败!"));
                }
            },1000);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            LogTraceUtils.error("callback",ex.getMessage(),ex);
        }
    }


    /**
     * 新增或者修改认证记录
     *
     * @param authLog 认证记录
     */
    private int insertOrUpdateAuthLog(AuthLog authLog) {
        AuthLog origin_loanauth = new AuthLog();
        origin_loanauth.setOrderNum(authLog.getOrderNum());
        origin_loanauth.setIdCard(authLog.getIdCard());
        origin_loanauth.setType(authLog.getType());
        origin_loanauth = authLogDao.getByAuthLog(origin_loanauth);
        if (origin_loanauth != null && origin_loanauth.getId()!=null) {
            authLog.setId(origin_loanauth.getId());
            authLog.setUpdateTime(DateUtils.getCurrentDateStr());
            return authLogDao.updateSelective(authLog);
        } else {
            authLog.setCreateTime(DateUtils.getCurrentDateStr());
            return authLogDao.insertAuthLog(authLog);
        }
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

    private boolean updateOrderFlowStatus(Order order, CheckflowTypeEnum academic, OrderFlowStatusEnum flowStatusEnum, OrderFlowStatusEnum updateFlowStatusEnum) {
        logger.info("updateOrderFlowStatus:order{};academic{};flowStatusEnum{};updateFlowStatusEnum{};",
                order,academic.getType(),flowStatusEnum,updateFlowStatusEnum);
        Map<String,Object> logMap = new HashMap<>();
        logMap.put("order",order);
        logMap.put("academic",academic.getType());
        logMap.put("flowStatusEnum",flowStatusEnum);
        logMap.put("updateFlowStatusEnum",updateFlowStatusEnum);
        logMap.put("log",LogTraceUtils.logReplace("updateOrderFlowStatus:order{};academic{};flowStatusEnum{};updateFlowStatusEnum{};",
                order.toString(), academic.getType(), flowStatusEnum, updateFlowStatusEnum ));
        LogTraceUtils.info("callback",logMap);
        OrderFlow orderFlow = this.getOrderFlow(
                order.getOrderNo(),academic);
        if (orderFlow == null || orderFlow.getId()==null ) {
            logger.error("更新orderFlow失败！orderFlow不存在");

            LogTraceUtils.error("callback","更新orderFlow失败！orderFlow不存在",
                    new DataDisposeException("更新orderFlow失败！orderFlow不存在"));
            return false;
        }
        if(flowStatusEnum==null || flowStatusEnum.getType().equals(orderFlow.getStatus())){
            orderFlow.setStatus(updateFlowStatusEnum.getType());
            logger.info("orderFlow:{}",orderFlow);
            if (orderFlowDao.update(orderFlow) != 1) {
                return false;
            }
            return true;
        }else{
            logger.error("更新orderFlow失败！orderFlow状态错误！flowStatusEnum：{};orderFlowStatus:{};",
                    flowStatusEnum,orderFlow.getStatus());
            LogTraceUtils.error("callback","更新orderFlow失败！orderFlow状态错误！flowStatusEnum："+
                            flowStatusEnum+";orderFlowStatus:"+orderFlow.getStatus()+";",
                    new DataDisposeException("更新orderFlow失败！orderFlow状态错误！"));
            return false;
        }


    }

    public void eductionCallBackOption(String error_message, String success, String error_code, String callback_set, String authType, String consistency)
    throws Exception{
        {
            Map<String,Object> logMap = new HashMap<>();
            logMap.put("error_message",error_message);
            logMap.put("success",success);
            logMap.put("error_code",error_code);
            logMap.put("callback_set",callback_set);
            logMap.put("authType",authType);
            logMap.put("consistency",consistency);
            logMap.put("log",LogTraceUtils.logReplace("收到回调请求error_message【{}】，success【{}】，error_code【{}】，callback_set【{}】，authType【{}】，consistency【{}】",
                    error_message, success, error_code, callback_set, authType, consistency ));
            LogTraceUtils.info("callback",logMap);
            logger.info("收到回调请求error_message【{}】，success【{}】，error_code【{}】，callback_set【{}】，authType【{}】，consistency【{}】",
                    error_message, success, error_code, callback_set, authType, consistency);
            if (callback_set.contains("%")) {
                callback_set = URLDecoder.decode(callback_set, "UTF-8");
            }

            String idCard = JsonUtils.getNodeValue(callback_set, "certNo");
            String orderNum = JsonUtils.getNodeValue(callback_set, "orderNum");
            AuthLog authLog = new AuthLog();
            authLog.setType(authType);
            authLog.setIdCard(idCard);
            authLog.setOrderNum(orderNum);
            authLog.setErrorMessage(error_message);
            Order order = new Order();
            order.setOrderNo(orderNum);
            order = orderDao.getByOrder(order);
            if(order==null){
                logger.error("订单不存在orderNum【{}】",orderNum);
                throw new DataDisposeException("订单不存在!");
            }
            CheckflowTypeEnum checkflowTypeEnum = null;
            CheckflowTypeEnum[] checkflowTypeEnums = CheckflowTypeEnum.values();
            for (CheckflowTypeEnum e:checkflowTypeEnums) {
                if(e.getType().equals(authType)){
                    checkflowTypeEnum=e;
                }
            }
            if(checkflowTypeEnum==null){
                logger.error("流程信息不存在authType【{}】",authType);
                throw new DataDisposeException("流程信息不存在!");
            }
            if (error_code.equals("SUCCESS") && success.equals("true") && "true".equals(consistency)) {
                // 认证通过
                authLog.setResult(ConstansUtils.SUCCESS);

                if(!this.updateOrderFlowStatus(
                        order,checkflowTypeEnum,OrderFlowStatusEnum.AUTH,OrderFlowStatusEnum.SUCCESS)){
                    throw new DataDisposeException("OrderFlow更新失败！");
                }
            } else {
                // 认证失败
                authLog.setResult(ConstansUtils.ERROR);

                if(!this.updateOrderFlowStatus(
                        order,checkflowTypeEnum,OrderFlowStatusEnum.AUTH,OrderFlowStatusEnum.FAIL)){
                    throw new DataDisposeException("OrderFlow更新失败！");
                }

                // 生成url
                String auth_url = bdCallbackhost + "h5/authorization.html"
                        + "?ordertoken=" + order.getOrderNo() + "&mid=" + order.getMerId()
                        + "&stp=0";
                smsComponent.sendSms(checkflowTypeEnum.getName()+"认证失败！请重新认证：" +
                        auth_url + "", order.getPhone());
            }
            if(this.insertOrUpdateAuthLog(authLog)!=1){
                throw new DataDisposeException("authLog更新失败！");
            }
        }
    }
}
