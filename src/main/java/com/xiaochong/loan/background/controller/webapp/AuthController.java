package com.xiaochong.loan.background.controller.webapp;


import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.bo.TongdunEducationDataBo;
import com.xiaochong.loan.background.entity.po.BankCard;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.entity.vo.yys.YysResultVoData;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.AuthService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import com.xiaochong.loan.background.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api("各种认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Resource(name = "authService")
    private AuthService authService;


    @ApiOperation(value = "认证信息获取", notes = "认证信息获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/orderAuth")
    public BaseResultVo<OrderAuthWebappVo> orderAuth(@RequestParam(value = "orderToken", required = false) String orderToken) {
        BaseResultVo<OrderAuthWebappVo> resultVo = new BaseResultVo<>();
        LogTrace.info("认证信息获取","orderToken"+orderToken);
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            resultVo.setResult(authService.orderAuth(orderToken));
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }



    @ApiOperation(value = "用户授权", notes = "用户授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "是否授权", required = true, dataType = "boolean", paramType = "form"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping("/userAuth")
    public BaseResultVo<Boolean> userAuth(@RequestParam(value = "orderToken") String orderToken,
                                          @RequestParam(value = "status") boolean status,
                                          @RequestParam(value = "latitude", required = false) String latitude,
                                          @RequestParam(value = "longitude", required = false) String longitude,
                                          HttpServletRequest request){
        logger.info("用户授权请求参数:{}","{orderToken:"+orderToken+"}");
        BaseResultVo<Boolean> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        String clientIp = WebUtils.getClientIP(request);
        logger.info("用户授权获取ip:{}",clientIp);
        if (StringUtils.isNotBlank(orderToken)){
            try {
                baseResultVo.setResult(authService.userAuth(
                        orderToken,status,latitude,longitude,clientIp));
            } catch (Exception e) {
                baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("用户授权失败！",e);
            }
        }else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "认证结束经纬度提交", notes = "认证结束经纬度提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping("/userEndAuth")
    public BaseResultVo<Boolean> userEndAuth(@RequestParam(value = "orderToken") String orderToken,
                                          @RequestParam(value = "latitude", required = false) String latitude,
                                          @RequestParam(value = "longitude", required = false) String longitude,
                                          HttpServletRequest request){
        logger.info("用户结束授权结束请求参数:{}","{orderToken:"+orderToken+"}");
        BaseResultVo<Boolean> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(orderToken)){
            try {
                baseResultVo.setResult(authService.userEndAuth(
                        orderToken,latitude,longitude));
            } catch (Exception e) {
                baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("用户结束授权结束失败！",e);
            }
        }else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "获取认证跳转", notes = "获取认证跳转")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/findNextCheckFlow")
    public BaseResultVo<CheckflowVo> findNextCheckFlow(@RequestParam(value = "orderToken") String orderToken) {
        BaseResultVo<CheckflowVo> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            resultVo.setResult(authService.findNextCheckFlow(orderToken));
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }



    @ApiOperation(value = "学历认证", notes = "通过学信网账号以及密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "account", value = "学信账号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "学信密码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/eductionAuth")
    public BaseResultVo<String> educationAuth(@RequestParam(value = "orderToken", required = false) String orderToken,
                                              @RequestParam(value = "account", required = false) String account,
                                              @RequestParam(value = "password", required = false) String password) {
        logger.info("学信认证开始，订单token:{}");
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken) && StringUtils.isNotBlank(account) && StringUtils.isNotBlank(password)) {
            try {
                resultVo.setResult(authService.educationAuth(orderToken, account, password));
            } catch (DataDisposeException e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            }
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }

    @ApiOperation(value = "学历认证(新)", notes = "通过学信网账号以及密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "account", value = "学信账号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "学信密码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/eductionAuthNew")
    public BaseResultVo<String> eductionAuthNew(@RequestParam(value = "orderToken", required = true) String orderToken,
                                              @RequestParam(value = "account", required = true) String account,
                                              @RequestParam(value = "password", required = true) String password) {
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken) && StringUtils.isNotBlank(account) && StringUtils.isNotBlank(password)) {
            try {
                resultVo.setResult(authService.educationAuthNew(orderToken, account, password));
            } catch (DataDisposeException e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("通过学信网账号以及密码认证：{}",e);
            }
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }

 @ApiOperation(value = "学历认证(同盾)", notes = "通过学信网账号以及密码认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "idCard", value = "身份证", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getTongdunEducation")
    public BaseResultVo<TongdunEducationDataBo> getTongdunEducation(@RequestParam(value = "orderToken", required = true) String orderToken,
                                                                    @RequestParam(value = "name", required = false) String name,
                                                                    @RequestParam(value = "idCard", required = false) String idCard) {
        BaseResultVo<TongdunEducationDataBo> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            try {
                resultVo.setResult(authService.getTongdunEducation(name,idCard,orderToken));
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("同盾学信认证失败：{}",e);
            }
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
     LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }

    @ApiOperation(value = "学历认证(新)，通过验证码获取", notes = "通过验证码获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "traceId", value = "traceId学信查询", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "verificationCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/eductionAuthByVerificationCode")
    public BaseResultVo<String> eductionAuthByVerificationCode(@RequestParam(value = "traceId", required = true) String traceId,
                                              @RequestParam(value = "verificationCode", required = true) String verificationCode,
                                              @RequestParam(value = "orderToken", required = true) String orderToken) {
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        logger.info("新学信验证码:"+verificationCode);
        if (StringUtils.isNotBlank(traceId) && StringUtils.isNotBlank(verificationCode) ) {
            try {
                resultVo.setResult(authService.eductionAuthByVerificationCode(orderToken,traceId, verificationCode));
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("通过验证码获取错误：{}",e);
            }
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }

    @ApiOperation(value = "学历认证(新)，更新验证码", notes = "更新验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/eductionUpdateVerificationCode")
    public BaseResultVo<String> eductionUpdateVerificationCode(@RequestParam(value = "orderToken", required = true) String orderToken) {
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            try {
                resultVo.setResult(authService.eductionUpdateVerificationCode(orderToken));
            } catch (Exception e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            }
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }

    @ApiOperation(value = "身份信息获取", notes = "身份信息获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getZhimaUserMessage")
    public BaseResultVo<Map<String,String>> getZhimaUserMessage(@RequestParam(value = "orderToken", required = false) String orderToken) {
        BaseResultVo<Map<String,String>> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            resultVo.setResult(authService.getZhimaUserMessage(orderToken));
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "提交芝麻信息", notes = "提交芝麻信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "realname", value = "真实姓名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "id_card", value = "身份证号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "mid", value = "商户id", required = true, dataType = "String", paramType = "form")
            })
    @PostMapping("/zhimaAuth")
    public BaseResultVo<String> zhimaAuth(@RequestParam(value = "ordertoken") String ordertoken,
                                                        @RequestParam(value = "realname") String realname,
                                                        @RequestParam(value = "id_card") String id_card,
                                                        @RequestParam(value = "phone") String phone,
                                                        @RequestParam(value = "mid") String mid) {
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(ordertoken) && StringUtils.isNotBlank(mid)) {
            try {
                resultVo.setResult(authService.zhimaAuth(ordertoken, realname, id_card, phone, mid));
            } catch (DataDisposeException e) {
                resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            }
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }



    @ApiOperation(value = "芝麻信息回调情况获取", notes = "芝麻信息回调情况获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getZhimaCallbackMessage")
    public BaseResultVo<AuthLogWebappVo> getZhimaCallbackMessage(@RequestParam(value = "orderToken", required = false) String orderToken) {
        BaseResultVo<AuthLogWebappVo> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            resultVo.setResult(authService.getZhimaCallbackMessage(orderToken));
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }



    @ApiOperation(value = "订单信息获取", notes = "订单信息获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderToken", value = "订单token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getOrderWebappVo")
    public BaseResultVo<OrderWebappVo> getOrderWebappVo(@RequestParam(value = "orderToken", required = false) String orderToken) {
        BaseResultVo<OrderWebappVo> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(orderToken)) {
            resultVo.setResult(authService.getOrderWebappVo(orderToken));
        } else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "运营商认证结果", notes = "手机运营商认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "手机服务密码", required = true, dataType = "String", paramType = "form")})
    @PostMapping("/certificate")
    public BaseResultVo<YysResultVoData> certificate(
            @RequestParam("ordertoken") String ordertoken,
            @RequestParam("password") String password) {
        LogTrace.info("运营商认证结果","ordertoken"+ordertoken);
        logger.info("运营商认证请求参数:{}", "{ordertoken:" + ordertoken + ",password:" + password + "}");
        BaseResultVo<YysResultVoData> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(ordertoken) && StringUtils.isNotBlank(password)) {
            try {
                baseResultVo.setResult(authService.certificate(ordertoken, password));
            } catch (DataDisposeException e) {
                baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            }
        } else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "提交验证码结果", notes = "提交短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "task_id", value = "task_id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "auth_code", value = "验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "trace_id", value = "trace_id", required = true, dataType = "String", paramType = "form")})
    @PostMapping("/yys_verify_code")
    public BaseResultVo<YysResultVoData> yysVerifyCode(
            @RequestParam("ordertoken") String ordertoken,
            @RequestParam("task_id") String task_id,
            @RequestParam("auth_code") String auth_code,
            @RequestParam("trace_id") String trace_id) {
        LogTrace.info("提交验证码结果","ordertoken"+ordertoken);
        logger.info("提交验证码请求参数:{}", "{ordertoken:" + ordertoken +
                ",task_id:" + task_id + ",auth_code:" + auth_code + ",trace_id:" + trace_id + "}");
        BaseResultVo<YysResultVoData> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(ordertoken) && StringUtils.isNotBlank(task_id) &&
                StringUtils.isNotBlank(auth_code) && StringUtils.isNotBlank(trace_id)) {
            baseResultVo.setResult(authService.yysVerifyCode(
                    ordertoken, task_id, auth_code, trace_id));
        } else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "提交联系人", notes = "提交联系人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "contactInfos", value = "联系人信息（JSON数组格式）", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "homeAddress", value = "家庭地址", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "province", value = "省", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "city", value = "市", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "area", value = "区", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "permanentAddress", value = "常住地址", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/submitContact")
    public BaseResultVo<String> submitContact(
            @RequestParam("ordertoken") String ordertoken,
            @RequestParam("contactInfos") String contactInfos,
            @RequestParam("homeAddress") String homeAddress,
            @RequestParam("province") String province,
            @RequestParam("city") String city,
            @RequestParam("area") String area,
            @RequestParam("permanentAddress") String permanentAddress) {
        logger.info("提交联系人");
        LogTrace.info("提交联系人","ordertoken"+ordertoken);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        try {
            baseResultVo.setResult(authService.submitContact(ordertoken,contactInfos,homeAddress,province,city,area,permanentAddress));
        } catch (DataDisposeException e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("提交联系人失败！",e);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    /**
     * 获取订单相关联系人信息
     *
     * @param ordertoken 订单token
     * @return 联系人结果
     */
    @ApiOperation(value = "获取联系人信息", notes = "获取联系人信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form")})
    @PostMapping(value = "/getContactInfo")
    public BaseResultVo<List<ContactInfoWebappVo>> getConatctInfo(
            @RequestParam("ordertoken") String ordertoken) {
        logger.info("获取联系人信息:{}", "{ordertoken:" + ordertoken + "}");
        LogTrace.info("获取联系人信息","ordertoken"+ordertoken);
        BaseResultVo<List<ContactInfoWebappVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(ordertoken)) {
            baseResultVo.setResult(authService.getConatctInfo(ordertoken));
        } else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "获取银行卡信息", notes = "获取银行卡信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form")})
    @PostMapping(value = "/getBankCard")
    public BaseResultVo<BankCard> getBankCard(
            @RequestParam("ordertoken") String ordertoken) {
        logger.info("获取银行卡信息参数:{}", "{ordertoken:" + ordertoken + "}");
        LogTrace.info("获取银行卡信息","ordertoken"+ordertoken);
        BaseResultVo<BankCard> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(ordertoken)) {
            baseResultVo.setResult(authService.getBankCard(ordertoken));
        } else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "提交银行卡信息", notes = "提交银行卡信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bankName", value = "银行名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bankPhone", value = "预留手机号", required = true, dataType = "String", paramType = "form")})
    @PostMapping("/submitBankCard")
    public BaseResultVo<String> submitBankCard(@RequestParam("ordertoken") String ordertoken,
                                                     @RequestParam("bankName") String bankName,
                                                     @RequestParam("bankCard") String bankCard,
                                                     @RequestParam("bankPhone") String bankPhone) {
        logger.info("提交银行卡信息请求参数:{}", "{ordertoken:" + ordertoken +
                ",bankName:" + bankName + ",bankCard:" + bankCard +
                ",bankPhone:" + bankPhone + "}");
        LogTrace.info("提交银行卡信息","ordertoken"+ordertoken);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(ordertoken) && StringUtils.isNotBlank(bankName) &&
                StringUtils.isNotBlank(bankCard) && StringUtils.isNotBlank(bankPhone)) {
            try {
                baseResultVo.setResult(authService.submitBankCard(ordertoken, bankName, bankCard, bankPhone));
            } catch (DataDisposeException e) {
                logger.error("更新绑卡信息失败！：{}", e);
            }
        } else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "提交银行卡信息(新)", notes = "提交银行卡信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ordertoken", value = "订单token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bankName", value = "银行名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bankPhone", value = "预留手机号", required = true, dataType = "String", paramType = "form") })
    @PostMapping("/insertBankCard")
    public BaseResultVo<String> insertBankCard(
            @RequestParam("ordertoken") String ordertoken,
            @RequestParam("bankName") String bankName,
            @RequestParam("bankCard") String bankCard,
            @RequestParam("bankPhone") String bankPhone) {
        BaseResultVo baseResultVo =new BaseResultVo();
        LogTrace.info("提交银行卡信息(新)","ordertoken"+ordertoken);
        BankCard bank = new BankCard();
        bank.setBankCard(bankCard);
        bank.setBankName(bankName);
        bank.setBankPhone(bankPhone);
        try {
            BusinessVo<String> businessVo=authService.insertBankCard(bank,ordertoken);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            logger.error("提交银行卡信息失败：{}",e);
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response",JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


}
