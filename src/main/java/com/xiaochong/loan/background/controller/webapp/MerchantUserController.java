package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.ProxyUser;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.ProxyUserService;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "前台账户管理接口")
@RestController
@RequestMapping("/pro/merchantuser")
public class MerchantUserController {

    private Logger logger = LoggerFactory.getLogger(MerchantUserController.class);

    @Resource(name = "proxyUserService")
    private ProxyUserService proxyUserService;


    @ApiOperation(value = "前台用户查询",notes = "通过商户名称和简称模糊分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "姓名",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "登录令牌",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/select")
    public BaseResultVo select(@RequestParam(name="username",required = false) String username,
                                              @RequestParam(name="status",required = false) String status,
                                              @RequestParam(name="token",required = true) String token,
                                              @RequestParam(name="pageNum",required = true) Integer pageNum,
                                              @RequestParam(name="pageSize",required = true) Integer pageSize){
        logger.info("前台用户查询请求参数：username：{}，status：{}，token：{}，pageNum：{}，pageSize：{}",username,status,token,pageNum,pageSize);
        BaseResultVo baseResultVo = new BaseResultVo();
        try {
            ProxyUser proxyUser = new ProxyUser();
            proxyUser.setUsername(username);
            proxyUser.setStatus(status);
            BusinessVo<BasePageInfoVo<ProxyUserManagerVo>> businessVo = proxyUserService.selectProxyUser(proxyUser,token,pageNum,pageSize);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("账号查询失败：{}",e);
        }
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "添加子账户",notes = "添加子账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "姓名",required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号",required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码",required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "登录令牌",required=true, dataType = "String", paramType = "query"),
    })
    @PostMapping("/insert")
    public BaseResultVo insert(@RequestParam(name="username",required = true) String username,
                                              @RequestParam(name="phone",required = true) String phone,
                                              @RequestParam(name="password",required = true) String password,
                                              @RequestParam(name="token",required = true) String token){
        BaseResultVo baseResultVo = new BaseResultVo();
        logger.info("添加子账户：username：{}，phone：{}，password：{}，token：{}",username,phone,password,token);
        try {
            ProxyUser proxyUser = new ProxyUser();
            proxyUser.setUsername(username);
            proxyUser.setPhone(phone);
            BusinessVo<String> businessVo=proxyUserService.addSonAccount(proxyUser,password,token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("账号查询失败！",e.getMessage());
        }
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "变更子账户状态",notes = "变更子账户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键",required=true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态",required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "登录令牌",required=true, dataType = "String", paramType = "query")
    })
    @PostMapping("/updateStatus")
    public BaseResultVo updateStatus(@RequestParam(name="id",required = true) Integer id,
                               @RequestParam(name="status",required = true) String status){
        BaseResultVo baseResultVo = new BaseResultVo();
        LogTrace.info("变更子账户状态","id:"+id);
        logger.info("添加子账户：id：{}，status：{}",id,status);
        try {
            ProxyUser proxyUser = new ProxyUser();
            proxyUser.setStatus(status);
            proxyUser.setId(id);
            BusinessVo<String> businessVo=proxyUserService.updateStatus(proxyUser);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("变更子账户状态失败！",e.getMessage());
        }
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

}
