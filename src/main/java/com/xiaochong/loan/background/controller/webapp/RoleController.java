package com.xiaochong.loan.background.controller.webapp;


import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.RoleWebappVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.ProxyUserService;
import com.xiaochong.loan.background.service.RoleWebappService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by wujiaxing on 2017/9/2.
 * 前台角色接口
 */
@Api(value = "前台角色接口")
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Resource(name = "proxyUserService")
    private ProxyUserService proxyUserService;

    @Resource(name = "roleWebappService")
    private RoleWebappService roleWebappService;

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;




    @ApiOperation(value = "角色分页列表",notes = "角色分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/rolePage")
    public BaseResultVo<BasePageInfoVo<RoleWebappVo>> rolePage(@RequestParam(value = "token") String token,
                                                                 @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                 @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("请求角色分页列表，参数为：{}","{pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<RoleWebappVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(roleWebappService.rolePage(token,pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求订单列表结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "角色列表",notes = "角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/roleList")
    public BaseResultVo<List<RoleWebappVo>> roleList(@RequestParam(value = "token") String token){
        logger.info("请求角色列表，参数为：{}","{token:"+token+"}");
        BaseResultVo<List<RoleWebappVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(roleWebappService.roleList(token));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求订单列表结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }



    @ApiOperation(value = "新增角色", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleRemark", value = "角色说明", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/addRole")
    public BaseResultVo<Boolean> addRole(@RequestParam("token")String token,
                                         @RequestParam("roleName")String roleName,
                                         @RequestParam("status")String status,
                                         @RequestParam("roleRemark")String roleRemark){
        logger.info("新增角色请求参数:{}","{ roleName:"+roleName+",token:"+
                token+",status:"+status+",roleRemark:"+roleRemark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,roleName,status,roleRemark);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        try {
            resultVo.setResult(roleWebappService.addRole(token,roleName,status,roleRemark));
        } catch (DataDisposeException e) {
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("新增角色请求失败：{}",e);
        }

        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }





    @ApiOperation(value = "修改角色", notes = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleRemark", value = "角色说明", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateRole")
    public BaseResultVo<Boolean> updateRole(@RequestParam("token")String token,
                                         @RequestParam("roleId")Integer roleId,
                                         @RequestParam(required = false,value = "roleName")String roleName,
                                         @RequestParam(required = false,value = "status")String status,
                                         @RequestParam(required = false,value = "roleRemark")String roleRemark){
        logger.info("修改角色请求参数:{}","{ roleName:"+roleName+",token:"+
                token+",status:"+status+",roleRemark:"+roleRemark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,roleId);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(roleWebappService.updateRole(token,roleId,roleName,status,roleRemark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }




}
