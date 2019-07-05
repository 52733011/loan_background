package com.xiaochong.loan.background.controller.manager;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.RoleManageVo;
import com.xiaochong.loan.background.service.RoleManageService;
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
 * Created by wujiaxing on 2017/9/4.
 * 后台角色管理接口
 */
@Api(value = "后台角色管理接口")
@RestController
@RequestMapping("/back/role")
public class RoleManageController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(RoleManageController.class);

    @Resource(name = "roleManageService")
    private RoleManageService roleManageService;



    @ApiOperation(value = "后台角色查询",notes = "后台角色查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/roleList")
    public BaseResultVo<List<RoleManageVo>> roleList(){
        logger.info("后台角色查询");
        BaseResultVo<List<RoleManageVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo.setResult(roleManageService.roleList());
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求订单列表结束，结果为：{}",baseResultVo);
        return baseResultVo;
    }




    @ApiOperation(value = "后台角色列表查询",notes = "后台角色列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/rolePage")
    public BaseResultVo<BasePageInfoVo<RoleManageVo>> rolePage(@RequestParam(value = "token") String token,
                                                               @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                               @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("后台角色列表查询，参数为：{}","{pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<RoleManageVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(roleManageService.rolePage(pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求订单列表结束，结果为：{}",baseResultVo);
        return baseResultVo;
    }



    @ApiOperation(value = "后台新增角色", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleRemark", value = "角色说明", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/addRole")
    public BaseResultVo<Boolean> addRole(@RequestParam("roleName")String roleName,
                                         @RequestParam("status")String status,
                                         @RequestParam("roleRemark")String roleRemark){
        logger.info("后台新增角色请求参数:{}","{ roleName:"+roleName
                +",status:"+status+",roleRemark:"+roleRemark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,roleName,status,roleRemark);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(roleManageService.addRole(roleName,status,roleRemark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }



    @ApiOperation(value = "后台修改角色接口", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色名", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleRemark", value = "角色说明", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateRole")
    public BaseResultVo<Boolean> updateRole(@RequestParam("roleId")Integer roleId,
                                            @RequestParam(required = false,value = "roleName")String roleName,
                                            @RequestParam(required = false,value = "status")String status,
                                            @RequestParam(required = false,value = "roleRemark")String roleRemark){
        logger.info("后台修改角色接口请求参数:{}","{ roleId:"+roleId+",roleName:"+roleName
                +",status:"+status+",roleRemark:"+roleRemark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,roleId);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(roleManageService.updateRole(roleId,roleName,status,roleRemark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }




}
