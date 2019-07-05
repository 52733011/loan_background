package com.xiaochong.loan.background.controller;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.service.VerificationService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
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
/**
 * Created by zhangyou on 2017/4/18.
 */
@Api(value = "短信验证码接口")
@RestController
@RequestMapping("/verification")
public class VerificationController {

    private Logger logger = LoggerFactory.getLogger(VerificationController.class);

    @Resource(name = "VerificationService")
    private VerificationService verificationService;

    /**
     * 发送验证码
     * @Autor zhangyou
     * @param type 类型
     * @return
     */
    @ApiOperation(value = "发送验证码", notes = "type值信息——注册:0,登录:1,更换手机号:2,")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/sendSmsCode")
    public BaseResultVo sendSmsCode(@RequestParam("phone")String phone, @RequestParam("type")Integer type){
        logger.info("发送验证码请求参数:{}","{phone:"+phone+",type:"+type+"}");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        BaseResultVo baseResultVo = new BaseResultVo();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        try {
            if(StringUtils.isNotBlank(phone) && type !=null){
                baseResultVo.setResult(verificationService.sendSmsCode(phone,type));
            }else{
                baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            }
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error(e.getMessage(),e);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "验证码校验", notes = "type值信息——注册:0,登录:1,更换手机号:2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "sms", value = "验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/checkVerification")
    public BaseResultVo checkVerification(@RequestParam("phone")String phone, @RequestParam("sms")String sms, @RequestParam("type")Integer type){
        logger.info("验证码校验请求参数:{}","{phone:"+phone+",type:"+type+"}");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        BaseResultVo baseResultVo = new BaseResultVo();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        try {
            if(StringUtils.isNotBlank(phone)&&StringUtils.isNotBlank(sms) && type !=null){
                baseResultVo.setResult(verificationService.checkVerification(phone,sms,type));
            }else{
                baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            }
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error(e.getMessage(),e);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }
}
