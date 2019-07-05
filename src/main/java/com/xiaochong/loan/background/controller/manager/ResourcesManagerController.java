package com.xiaochong.loan.background.controller.manager;


import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.ResourcesManageService;
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
 * 后台权限管理接口
 */
@Api(value = "后台权限管理接口")
@RestController
@RequestMapping("/back/resources")
public class ResourcesManagerController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ResourcesManagerController.class);

    @Resource(name = "resourcesWebappService")
    private ResourcesWebappService resourcesWebappService;

    @Resource(name = "resourcesManageService")
    private ResourcesManageService resourcesManageService;


    @ApiOperation(value = "后台查询webapp权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "merchId", value = "商户id", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "types", value = "权限类型 1:菜单 2：接口 3:工具，用逗号分隔", required = false, dataType = "String", paramType = "form")
    })
    @PostMapping("/listResourcesWebapp")
    public BaseResultVo<List<ResourcesWebappVo>> listResourcesWebapp(@RequestParam(required = false,value = "merchId")Integer merchId,
                                                                     @RequestParam(required = false,value = "types")String types){
        logger.info("后台查询webapp权限请求");
        BaseResultVo<List<ResourcesWebappVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo.setResult(resourcesWebappService.listResourcesWebapp(merchId,types));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }



    @ApiOperation(value = "Webapp权限管理列表",notes = "Webapp权限管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "resName", value = "权限名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "resUrl", value = "资源url", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父节点", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "权限类型   1:菜单    2：接口", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/resourcesWebappPage")
    public BaseResultVo<BasePageInfoVo<ResourcesWebappPage>> resourcesWebappPage(@RequestParam(value = "token") String token,
                                                                                 @RequestParam(required = false,value = "resName") String resName,
                                                                                 @RequestParam(required = false,value = "resUrl") String resUrl,
                                                                                 @RequestParam(required = false,value = "parentId") Integer parentId,
                                                                                 @RequestParam(required = false,value = "type") Integer type,
                                                                                 @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                                 @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("请求Webapp权限管理列表，参数为：{}","{pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<ResourcesWebappPage>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(resourcesWebappService.resourcesWebappPage(parentId, type, resName, resUrl, pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求Webapp权限管理列表结束，结果为：{}",baseResultVo);
        return baseResultVo;
    }




    @ApiOperation(value = "后台新增webapp权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "菜单名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "url", value = "url", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "状态 1菜单 2接口 3应用", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父节点", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "菜单说明", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/addResourcesWebapp")
    public BaseResultVo<Boolean> addResourcesWebapp(@RequestParam("name")String name,
                                         @RequestParam("url")String url,
                                         @RequestParam("status")String status,
                                         @RequestParam("type")Integer type,
                                         @RequestParam("parentId")Integer parentId,
                                         @RequestParam("remark")String remark){
        logger.info("后台新增webapp权限请求参数:{}","{ name:"+name+",url:"+url+
                ",status:"+status+",type:"+type+",parentId:"+parentId+",remark:"+remark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,name,url,status,type,parentId,remark);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.addResourcesWebapp(name,url,status,type,parentId,remark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }




    @ApiOperation(value = "后台修改webapp权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "菜单名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "url", value = "url", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "状态 1菜单 2接口", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父节点", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "菜单说明", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateResourcesWebapp")
    public BaseResultVo<Boolean> updateResourcesWebapp(@RequestParam("id")Integer id,
                                                       @RequestParam(required = false,value = "name")String name,
                                                       @RequestParam(required = false,value = "url")String url,
                                                       @RequestParam(required = false,value = "status")String status,
                                                       @RequestParam(required = false,value = "type")Integer type,
                                                       @RequestParam(required = false,value = "parentId")Integer parentId,
                                                       @RequestParam(required = false,value = "remark")String remark){
        logger.info("后台修改webapp权限请求参数:{}","{ name:"+name+",url:"+url+
                ",status:"+status+",type:"+type+",parentId:"+parentId+",remark:"+remark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,id);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesWebappService.updateResourcesWebapp(id,name,url,status,type,parentId,remark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }



    @ApiOperation(value = "前台菜单权限绑定", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "menuId", value = "菜单id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "resourcesIds", value = "权限id,用逗号隔开", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/resourcesMenuWebapp")
    public BaseResultVo<Boolean> resourcesMenuWebapp(@RequestParam("menuId")Integer menuId,
                                                 @RequestParam("resourcesIds")String resourcesIds){
        logger.info("前台菜单权限绑定请求参数:{}","{ menuId:"+menuId+",resourcesId:"+resourcesIds+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo.setResult(resourcesWebappService.resourcesMenuWebapp(menuId,resourcesIds));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }




    @ApiOperation(value = "后台菜单权限绑定", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "menuId", value = "菜单id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "resourcesIds", value = "权限id,用逗号隔开", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/resourcesMenuManage")
    public BaseResultVo<Boolean> resourcesMenuManage(@RequestParam("menuId")Integer menuId,
                                                 @RequestParam("resourcesIds")String resourcesIds){
        logger.info("后台菜单权限绑定请求参数:{}","{ menuId:"+menuId+",resourcesId:"+resourcesIds+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo.setResult(resourcesManageService.resourcesMenuManage(menuId,resourcesIds));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }





    @ApiOperation(value = "后台查询Manage权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "权限类型   1:菜单    2：接口", required = false, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/listResourcesManage")
    public BaseResultVo<List<ResourcesManageVo>> listResourcesManage(@RequestParam(required = false,value = "type")Integer type){
        logger.info("后台查询Manage权限请求");
        BaseResultVo<List<ResourcesManageVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo.setResult(resourcesManageService.listResourcesManage(type));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }




    @ApiOperation(value = "后台查询用户权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "权限类型   1:菜单    2：接口", required = false, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/listResourcesManageUser")
    public BaseResultVo<List<ResourcesManageVo>> listResourcesManageUser(@RequestParam(value = "token")String token,
                                                                     @RequestParam(required = false,value = "type")Integer type){
        logger.info("后台查询用户权限请求");
        BaseResultVo<List<ResourcesManageVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        resultVo = this.paramsNotNullReturn(resultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesManageService.listResourcesManageUser(token,type));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }


    @ApiOperation(value = "manage权限管理列表",notes = "manage权限管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "resName", value = "权限名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "resUrl", value = "资源url", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父节点", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "权限类型   1:菜单    2：接口", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/resourcesManagePage")
    public BaseResultVo<BasePageInfoVo<ResourcesManagePage>> resourcesManagePage(@RequestParam(value = "token") String token,
                                                                                 @RequestParam(required = false,value = "resName") String resName,
                                                                                 @RequestParam(required = false,value = "resUrl") String resUrl,
                                                                                 @RequestParam(required = false,value = "parentId") Integer parentId,
                                                                                 @RequestParam(required = false,value = "type") Integer type,
                                                                                 @RequestParam(value = "pageNum")Integer pageNum,
                                                                                 @RequestParam(value = "pageSize")Integer pageSize){
        logger.info("请求manage权限管理列表，参数为：{}","{pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<ResourcesManagePage>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(resourcesManageService.resourcesManagePage(resName,resUrl,parentId,type,pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求manage权限管理列表结束，结果为：{}",baseResultVo);
        return baseResultVo;
    }


    @ApiOperation(value = "后台查询Manage角色权限及选中情况", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "权限类型   1:菜单    2：接口", required = false, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/listRoleResourcesManage")
    public BaseResultVo<List<RoleResourcesManageVo>> listRoleResourcesManage(@RequestParam("roleId")Integer roleId,
                                                                             @RequestParam("type")Integer type){
        logger.info("后台查询Manage角色权限请求");
        BaseResultVo<List<RoleResourcesManageVo>> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo.setResult(resourcesManageService.listRoleResourcesManage(roleId,type));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }




    @ApiOperation(value = "后台新增manage权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "菜单名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "url", value = "url", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "状态 1菜单 2接口", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父节点", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "菜单说明", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/addResourcesManage")
    public BaseResultVo<Boolean> addResourcesManage(@RequestParam("name")String name,
                                         @RequestParam("url")String url,
                                         @RequestParam("status")String status,
                                         @RequestParam("type")Integer type,
                                         @RequestParam("parentId")Integer parentId,
                                         @RequestParam("remark")String remark){
        logger.info("后台新增manage权限请求参数:{}","{ name:"+name+",url:"+url+
                ",status:"+status+",type:"+type+",parentId:"+parentId+",remark:"+remark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,name,url,status,type,parentId,remark);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesManageService.addResourcesManage(name,url,status,type,parentId,remark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }





    @ApiOperation(value = "后台修改manage权限", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "id", value = "id", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "菜单名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "url", value = "url", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "状态 1菜单 2接口", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父节点", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "菜单说明", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateResourcesManage")
    public BaseResultVo<Boolean> updateResourcesManage(@RequestParam("id")Integer id,
                                                       @RequestParam(required = false,value = "name")String name,
                                                       @RequestParam(required = false,value = "url")String url,
                                                       @RequestParam(required = false,value = "status")String status,
                                                       @RequestParam(required = false,value = "type")Integer type,
                                                       @RequestParam(required = false,value = "parentId")Integer parentId,
                                                       @RequestParam(required = false,value = "remark")String remark){
        logger.info("后台修改manage权限请求参数:{}","{ id:"+id+",name:"+url+",name:"+url+
                ",status:"+status+",type:"+type+",parentId:"+parentId+",remark:"+remark+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,id);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        resultVo.setResult(resourcesManageService.updateResourcesManage(id,name,url,status,type,parentId,remark));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }




    @ApiOperation(value = "后台角色权限分配", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "resourcesIds", value = "权限id,用逗号隔开", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/resourcesAssign")
    public BaseResultVo<Boolean> resourcesAssign(@RequestParam("roleId")Integer roleId,
                                                 @RequestParam("resourcesIds")String resourcesIds){
        logger.info("后台角色权限分配请求参数:{}","{ roleId:"+roleId+",resourcesIds:"+resourcesIds+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,roleId,resourcesIds);
        if(!this.paramsNotNullReturn(roleId,resourcesIds)){
            resultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            resultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
            return resultVo;
        }

        try {
            resultVo.setResult(resourcesManageService.resourcesAssign(roleId,resourcesIds));
        } catch (DataDisposeException e) {
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("后台角色权限分配请求参数失败：{}",e);
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }






    @ApiOperation(value = "后台角色菜单权限分配", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "menuIds", value = "权限id,用逗号隔开", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/resourcesAssignMenu")
    public BaseResultVo<Boolean> resourcesAssignMenu( @RequestParam("roleId")Integer roleId,
                                                 @RequestParam("menuIds")String menuIds){
        logger.info("后台角色菜单权限分配请求参数:{}","{ roleId:"+roleId+",menuIds:"+menuIds+"}");
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        if(!this.paramsNotNullReturn(roleId,menuIds)){
            resultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            resultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
            return resultVo;
        }

        try {
            resultVo.setResult(resourcesManageService.resourcesAssignMenu(roleId,menuIds));
        } catch (DataDisposeException e) {
            resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("后台角色菜单权限分配请求参数失败：{}",e);
        }
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        return resultVo;
    }



}
