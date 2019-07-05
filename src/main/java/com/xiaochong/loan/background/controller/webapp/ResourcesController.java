package com.xiaochong.loan.background.controller.webapp;


import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.MenuWebappVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.ResourcesWebappService;
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
 *
 */
@Api(value = "权限接口")
@RestController
@RequestMapping("/resources")
public class ResourcesController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ResourcesController.class);

    @Resource(name = "resourcesWebappService")
    private ResourcesWebappService resourcesWebappService;

    @ApiOperation(value = "查询用户菜单", notes = "token=查询结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/queryMenu")
    public BaseResultVo<List<MenuWebappVo>> queryMenu(@RequestParam("token")String token){
        logger.info("查询用户菜单请求参数:{}","{token:"+token+"}");
        BaseResultVo<List<MenuWebappVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.queryMenu(token));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "查询用户工具", notes = "token=查询结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/queryTool")
    public BaseResultVo<List<MenuWebappVo>> queryTool(@RequestParam("token")String token){
        logger.info("查询用户工具请求参数:{}","{token:"+token+"}");
        BaseResultVo<List<MenuWebappVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.queryTool(token));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }



    @ApiOperation(value = "查询用户按钮", notes = "token=查询结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/queryButton")
    public BaseResultVo<List<MenuWebappVo>> queryButton(@RequestParam("token")String token){
        logger.info("查询用户按钮请求参数:{}","{token:"+token+"}");
        BaseResultVo<List<MenuWebappVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.queryButton(token));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "查询角色权限", notes = "token=查询结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/queryMenuByRoleId")
    public BaseResultVo<List<MenuWebappVo>> queryMenuByRoleId(@RequestParam("roleId")String roleId){
        logger.info("查询角色权限请求参数:{}","{roleId:"+roleId+"}");
        BaseResultVo<List<MenuWebappVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,roleId);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.queryMenuByRoleId(roleId));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }





    @ApiOperation(value = "查询商户角色权限", notes = "token=查询结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "types", value = "权限类型（1 菜单 2 接口 3 工具，用逗号分隔）", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/queryMerchMenu")
    public BaseResultVo<List<MenuWebappVo>> queryMerchMenu(@RequestParam("token")String token,
                                                           @RequestParam(required = false,value = "roleId")Integer roleId,
                                                           @RequestParam(required = false,value = "types")String types){
        logger.info("查询商户角色权限请求参数:{}","{token:"+token+"}");
        BaseResultVo<List<MenuWebappVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.queryMerchMenu(token,roleId,types));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }




    @ApiOperation(value = "权限分配", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "resourcesIds", value = "权限id,用逗号隔开", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/resourcesAssign")
    public BaseResultVo<Boolean> resourcesAssign(@RequestParam("token")String token,
                                         @RequestParam("roleId")Integer roleId,
                                         @RequestParam("resourcesIds")String resourcesIds){
        logger.info("企业前端权限分配请求参数:{}","{ roleId:"+roleId+",token:"+token+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,roleId,resourcesIds);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        try {
            resultVo.setResult(resourcesWebappService.resourcesAssign(token,roleId,resourcesIds));
        } catch (DataDisposeException e) {
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("企业前端权限分配请求参数失败：{}",e);
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }






    @ApiOperation(value = "菜单权限分配", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "menuIds", value = "权限id,用逗号隔开", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/resourcesAssignMenu")
    public BaseResultVo<Boolean> resourcesAssignMenu(@RequestParam("token")String token,
                                         @RequestParam("roleId")Integer roleId,
                                         @RequestParam("menuIds")String menuIds){
        logger.info("菜单权限分配请求参数:{}","{ roleId:"+roleId+",token:"+token+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,token,roleId,menuIds);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        try {
            resultVo.setResult(resourcesWebappService.resourcesAssignMenu(token,roleId,menuIds));
        } catch (DataDisposeException e) {
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("菜单权限分配请求失败：{}",e);
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }




}
