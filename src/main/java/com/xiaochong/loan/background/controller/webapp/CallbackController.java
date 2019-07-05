package com.xiaochong.loan.background.controller.webapp;


import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.service.AuthLogService;
import com.xiaochong.loan.background.utils.enums.CheckflowTypeEnum;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import com.xiaochong.loan.background.utils.RiskConstantsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api("各种认证回调接口")
@RestController
@RequestMapping("/callback")
public class CallbackController {

    private Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Resource(name = "authLogService")
    private AuthLogService authLogService;

    @ApiOperation(value = "学信网回调信息", notes = "学信网回调信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "error_message", value = "错误消息 ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "success", value = "是否成功 ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "error_code", value = "错误编码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callback_set", value = "回调参数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "consistency", value = "一致性", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/eductionCallBack")
    public BaseResultVo<String> eductionCallBack(@RequestParam("error_message") String error_message,
                                                 @RequestParam("callback_set") String callback_set,
                                                 @RequestParam("success") String success,
                                                 @RequestParam("error_code") String error_code,
                                                 @RequestParam(required = false,value = "consistency") String consistency) {
        logger.info("学信认证回调:{},{},{},{}", error_message, success, error_code, callback_set,LogTrace.getTrace().getTraceId());

        BaseResultVo<String> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(error_message) && StringUtils.isNotBlank(success) &&
                StringUtils.isNotBlank(error_code) && StringUtils.isNotBlank(callback_set)){
            try {
                authLogService.eductionCallBackOption(error_message,success,error_code,callback_set, RiskConstantsUtils.EDUCATION_TYPE,consistency);
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("学信网认证回调失败！",e);
            }
        }
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "芝麻回调信息", notes = "芝麻回调信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "open_id", value = "芝麻open_id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "error_message", value = "错误消息 ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "success", value = "是否成功 ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "error_code", value = "错误编码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callback_set", value = "回调参数", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/zhimaCallback")
    public BaseResultVo<String> zhimaCallBack(@RequestParam("open_id") String open_id,
                              @RequestParam("error_message") String error_message,
                              @RequestParam("success") String success,
                              @RequestParam("error_code") String error_code,
                              @RequestParam("callback_set") String callback_set) {
        logger.info("芝麻回调callback:{},{},{},{},{}", open_id, error_message,
                success, error_code, callback_set,LogTrace.getTrace().getTraceId());
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(error_message) && StringUtils.isNotBlank(success) &&
                StringUtils.isNotBlank(error_code) && StringUtils.isNotBlank(callback_set)){
            try {
                authLogService.callBackOption(error_message,success,error_code,callback_set, CheckflowTypeEnum.ZHIMA.getType());
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("芝麻回调失败！",e);
            }

        }
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "运营商认证的通知结果", notes = "运营商认证结果的回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "error_message", value = "error_message", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "success", value = "success ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "error_code", value = "error_code", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callback_set", value = "callback_set", required = true, dataType = "String", paramType = "query")})
    @GetMapping("/yys_callback")
    public BaseResultVo<String> yysCallback(@RequestParam("error_message") String error_message,
                            @RequestParam("success") String success,
                            @RequestParam("error_code") String error_code,
                            @RequestParam("callback_set") String callback_set) {
        logger.info("运营商回调参数:{}", "{error_message:" + error_message +
                ",success:" + success + ",error_code:" + error_code + ",callback_set:" + callback_set + ",traceId"+LogTrace.getTrace().getTraceId()+"}");

        BaseResultVo<String> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(error_message) && StringUtils.isNotBlank(success) &&
                StringUtils.isNotBlank(error_code) && StringUtils.isNotBlank(callback_set)) {
            try {
                authLogService.callBackOption(error_message, success, error_code, callback_set, CheckflowTypeEnum.OPERATOR.getType());
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("芝麻回调失败！",e);
            }
        }
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "运营商报告回调通知", notes = "运营商认证结果的回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yys_report_url", value = "yys_report_url", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderNum", value = "orderNum ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "yys_report_success", value = "error_code", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/yys_report_callback")
    public BaseResultVo<String> yysReportCallback(@RequestParam("yys_report_url")String yys_report_url,
                                                  @RequestParam("orderNum") String orderNum,
                                                  @RequestParam("yys_report_success") String yys_report_success,
                                                  @RequestParam("id_card")String id_card) {
        logger.info("运营商报告回调:{}", "{yys_report_url:" + yys_report_url +
                ",orderNum:" + orderNum + ",yys_report_success:" + yys_report_success + ",id_card:"+id_card+ ",traceId"+LogTrace.getTrace().getTraceId()+"}");

        BaseResultVo<String> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(yys_report_url) && StringUtils.isNotBlank(orderNum) &&
                StringUtils.isNotBlank(yys_report_success) && StringUtils.isNotBlank(id_card)) {
            try {
                authLogService.yysReportCallBack(yys_report_url, orderNum, yys_report_success, id_card, CheckflowTypeEnum.OPERATOR_REPORT.getType());
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("芝麻回调失败！",e);
            }
        }
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "提交到risk后订单回调", notes = "提交到risk后订单回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "error_message", value = "error_message", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "success", value = "success ", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "error_code", value = "error_code", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callback_set", value = "callback_set", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "callback_data", value = "callback_data", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/riskOrderCallBack")
    public void riskOrderCallBack(
            @RequestParam("error_message") String error_message,
            @RequestParam("callback_set") String callback_set,
            @RequestParam("callback_data") String callback_data,
            @RequestParam("success") String success,
            @RequestParam("error_code") String error_code) {
        LogTrace.info("提交到risk后订单回调:","{error_message:" + error_message +
                ",callback_set:" + callback_set + ",callback_data:" + callback_data + ",success:"+success+ ",error_code:"+error_code);
        logger.info("提交到risk后订单回调:" +
                "error_message:【{}】,callback_set:【{}】,callback_data:【{}】,success:【{}】,error_code【{}】,traceId【{}】",
                error_message,callback_set,callback_data,success,error_code,LogTrace.getTrace().getTraceId());
        if (StringUtils.isNotBlank(success) && StringUtils.isNotBlank(error_code) && StringUtils.isNotBlank(callback_set) &&
                StringUtils.isNotBlank(callback_data)){
            authLogService.riskOrderCallback(callback_data,success,error_code,callback_set);
        }else {
            logger.info("参数错误未进入异步");
        }
    }

}
