package com.xiaochong.loan.background.controller.webapp;


import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.service.ProxyUserService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
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
 * Created by wujiaxing on 2017/8/8.
 */
@Api(value = "用户接口")
@RestController
@RequestMapping("/proxyUser")
public class ProxyUserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ProxyUserController.class);

    @Resource(name = "proxyUserService")
    private ProxyUserService proxyUserService;

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;

    @ApiOperation(value = "用户登录",notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "用户名", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "图形验证码",required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "sms", value = "短信验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "time", value = "时间戳", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/userLogin")
    public BaseResultVo<String> userLogin(@RequestParam("phone")String phone,
                                  @RequestParam("password")String password,
                                  @RequestParam("code") String code,
                                  @RequestParam("sms") String sms,
                                  @RequestParam("time")String time){

        logger.info("查询用户信息请求参数:{}","{phone:"+phone+", password:"+
                password+", code:"+code+", sms:"+sms+", time:"+time+"}");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        String resultCode = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(password) &&
                StringUtils.isNotBlank(code) && StringUtils.isNotBlank(sms) &&
                StringUtils.isNotBlank(time)){
            BusinessVo<String> businessVo = proxyUserService.userLogin(phone, password, code, sms, time);
            baseResultVo.setResult(businessVo);
        }else {
            resultCode = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        baseResultVo.setCode(resultCode);
        baseResultVo.setMessage(message);
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }



    @ApiOperation(value = "发送验证码",notes = "发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "用户名", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码",required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "time", value = "时间戳", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/sendSmsCode")
    public BaseResultVo<Boolean> sendSmsCode(@RequestParam("phone")String phone,
                                    @RequestParam("code") String code,
                                    @RequestParam("time")String time){
        BaseResultVo<Boolean> baseResultVo = new BaseResultVo<>();
        String resultCode = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code) &&
                StringUtils.isNotBlank(time)){
            try {
                baseResultVo.setResult(proxyUserService.sendSmsCode(phone, code, time));
            } catch (OKhttpException | DataDisposeException e) {
                logger.error(e.getMessage(),e);
                resultCode = ResultConstansUtil.SYSTEM_ERROR_CODE;
                message = ResultConstansUtil.SYSTEM_ERROR_DESC;
            }
        }else {
            resultCode = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        baseResultVo.setCode(resultCode);
        baseResultVo.setMessage(message);
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "忘记密码发送验证码",notes = "忘记密码发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "用户名", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "merchantName", value = "公司名称",required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/sendSmsCodeForForgetPassword")
    public BaseResultVo<Boolean> sendSmsCodeForForgetPassword(@RequestParam("phone")String phone,
                                    @RequestParam("merchantName") String merchantName){
        BaseResultVo<Boolean> baseResultVo = new BaseResultVo<>();
        String resultCode = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(merchantName)){
            try {
                baseResultVo.setResult(proxyUserService.sendSmsCodeForForgetPassword(phone, merchantName));
            } catch (OKhttpException | DataDisposeException e) {
                logger.error(e.getMessage(),e);
                resultCode = ResultConstansUtil.SYSTEM_ERROR_CODE;
                message = ResultConstansUtil.SYSTEM_ERROR_DESC;
            }
        }else {
            resultCode = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        baseResultVo.setCode(resultCode);
        baseResultVo.setMessage(message);
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    /**
     * 查询企业信息
     * @return BaseResultVo
     */
    @ApiOperation(value = "查询用户信息", notes = "token=查询结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/queryCompanyUser")
    public BaseResultVo<ProxyUserVo> ProxyUser(@RequestParam("token")String token){
        logger.info("查询用户信息请求参数:{}","{token:"+token+"}");
        BaseResultVo<ProxyUserVo> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(proxyUserService.queryProxyUserByToken(token));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }



    /**
     * 忘记密码
     * @return BaseResultVo
     */
    @ApiOperation(value = "忘记密码", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantName", value = "商户名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "sms", value = "验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/forgetPassword")
    public BaseResultVo<Boolean> forgetPassword(@RequestParam("phone")String phone,
                                       @RequestParam("merchantName")String merchantName,
                                       @RequestParam("sms")String sms,
                                       @RequestParam("password")String password){
        logger.info("忘记密码请求参数:{}","{phone:"+phone+", sms:"+sms+", password:"+password+"}");
        LogTrace.info("忘记密码","phone"+phone);
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        if(StringUtils.isBlank(phone)||StringUtils.isBlank(sms)||StringUtils.isBlank(password)){
            resultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            resultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
            return resultVo;
        }
        resultVo.setResult(proxyUserService.forgetPassword(merchantName,phone,sms,password));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",resultVo.toString());
        return resultVo;
    }

    /**
     * 登录密码修改
     * @return BaseResultVo
     */
    @ApiOperation(value = "登录密码修改", notes = " 登录密码修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/updatePassword")
    public BaseResultVo<String> updatePassword(@RequestParam("token")String token,
                                                @RequestParam("password")String password,
                                                  @RequestParam("oldPassword")String oldPassword){
        logger.info("登陆密码修改");
        LogTrace.info("登录密码修改","");
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        try{
            resultVo = this.paramsNotNullReturn(resultVo,token,password,oldPassword);
            if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
                return resultVo;
            }
            String userId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
            if(StringUtils.isBlank(userId)){
                resultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
                resultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
                return resultVo;
            }
            BusinessVo<String> businessVo=proxyUserService.updatePassword(Integer.valueOf(userId),password,oldPassword);
            resultVo.setResult(businessVo);
        }catch (Exception e){
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("密码修改失败！",e);
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",resultVo.toString());
        return resultVo;
    }

    /**
     * 首次登录密码修改
     * @return BaseResultVo
     */
    @ApiOperation(value = "首次登录密码设置", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/firstSetPassword")
    public BaseResultVo<Boolean> firstSetPassword(@RequestParam("token")String token,
                                                @RequestParam("password")String password){
        logger.info("初次登录密码设置请求参数:{}","{ password:"+password+",token:"+token+"}");
        LogTrace.info("首次登录密码设置","");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,password);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        String userId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        if(StringUtils.isBlank(userId)){
            resultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
            resultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
            return resultVo;
        }
        resultVo.setResult(proxyUserService.firstSetPassword(Integer.valueOf(userId),password));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",resultVo.toString());
        return resultVo;
    }




    @ApiOperation(value = "子用户列表",notes = "子用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/subUserPage")
    public BaseResultVo<BasePageInfoVo<SubUserWebappVo>> subUserPage(@RequestParam(value = "token") String token,
                                                                     @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                     @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("请求子用户列表，参数为：{}","{pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        LogTrace.info("子用户列表","");
        BaseResultVo<BasePageInfoVo<SubUserWebappVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(proxyUserService.subUserPage(token,pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求子用户列表结束，结果为：{}",baseResultVo);
        LogTrace.info("response",baseResultVo.toString());
        return baseResultVo;
    }


    @ApiOperation(value = "新增子账户", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userName", value = "姓名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pwd", value = "初始密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/addSubUser")
    public BaseResultVo<Boolean> addSubUser(@RequestParam("token")String token,
                                            @RequestParam("userName")String userName,
                                            @RequestParam("phone")String phone,
                                            @RequestParam("pwd")String pwd,
                                            @RequestParam("roleId")Integer roleId){
        logger.info("初新增子账户请求参数:{}","{ token:"+token+",userName:"+userName+
                ",phone:"+phone+",pwd:"+pwd+",roleId:"+roleId+"}");
        LogTrace.info("新增子账户","");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,userName,phone,pwd,roleId);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }

        try {
            resultVo.setResult(proxyUserService.addSubUser(token,userName,phone,pwd,roleId));
        } catch (Exception e) {
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("新增子账户失败：{}",e);
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",resultVo.toString());
        return resultVo;
    }



    @ApiOperation(value = "修改子账户", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/updateSubUser")
    public BaseResultVo<Boolean> updateSubUser(@RequestParam("token")String token,
                                            @RequestParam("userId")Integer userId,
                                            @RequestParam("roleId")Integer roleId){
        logger.info("修改子账户请求参数:{}","{ token:"+token+",userId:"+userId+",roleId:"+roleId+"}");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,userId,roleId);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }

        resultVo.setResult(proxyUserService.updateSubUser(token,userId,roleId));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response",resultVo.toString());
        return resultVo;
    }



}
