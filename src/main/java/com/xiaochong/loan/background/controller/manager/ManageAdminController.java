package com.xiaochong.loan.background.controller.manager;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.ManagerAdminVo;
import com.xiaochong.loan.background.service.ManageAdminService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jinxin on 2017/8/14.
 * 后台登录
 */
@Api(value = "后台用户登录接口")
@RestController
@RequestMapping("/back/admin")
public class ManageAdminController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(ManageAdminController.class);

    @Autowired
    private ManageAdminService manageAdminService;

    /**
     * 后台用户登录
     * @param userName 用户名
     * @param password 密码
     * @return 操作结果
     */
    @ApiOperation(value = "后台用户登录接口",notes = "账号+密码，返回token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "time", value = "时间戳", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/login")
    public BaseResultVo<String> managerLogin(@RequestParam(value = "userName")String userName,
                                             @RequestParam(value = "password")String password,
                                             @RequestParam(value = "code") String code,
                                             @RequestParam(value = "time")String time){
        logger.info("后台用户登录：userName:{},password:{},code:{},time:{}",userName
                ,password,code,time);
        BaseResultVo<String> result = new BaseResultVo<>();
        try{
            if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(password)&&StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(time)){
                BusinessVo<String> businessVo = manageAdminService.managerLogin(userName, password, code, time);
                result.setResult(businessVo);
                result.setCode(ResultConstansUtil.SUCCESS_CODE);
                result.setMessage(ResultConstansUtil.SUCCESS_DESC);
            }else {
                result.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                result.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            }
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("后台用户登录接口！",e);
        }
        result.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "后台用户新增接口",notes = "后台用户新增接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "初始密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/addManagerAdmin")
    public BaseResultVo<Boolean> addManagerAdmin(@RequestParam(value = "userName")String userName,
                                             @RequestParam(value = "password")String password,
                                             @RequestParam(required = false,value = "phone")String phone,
                                             @RequestParam(value = "roleId") Integer roleId){
        logger.info("后台用户新增：userName:{},password:{},phone:{},roleId:{}",
                userName,password,phone,roleId);
        BaseResultVo<Boolean> result = new BaseResultVo<>();
        result.setCode(ResultConstansUtil.SUCCESS_CODE);
        result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        try{
            result = this.paramsNotNullReturn(result,userName,password,roleId);
            if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(result.getCode())){
                return result;
            }
            BusinessVo<Boolean> businessVo =
                    manageAdminService.addManagerAdmin(userName, password, phone, roleId);
            result.setResult(businessVo);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("后台用户新增失败！",e);
        }
        result.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }




    @ApiOperation(value = "后台用户修改接口",notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "是否启用", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateManagerAdmin")
    public BaseResultVo<Boolean> updateManagerAdmin(@RequestParam(value = "userId")Integer userId,
                                                    @RequestParam(required = false,value = "roleId") Integer roleId,
                                                    @RequestParam(required = false,value = "status") String status){
        logger.info("后台用户修改：userId:{},roleId:{},status{}",
                userId,roleId,status);
        BaseResultVo<Boolean> result = new BaseResultVo<>();
        result.setCode(ResultConstansUtil.SUCCESS_CODE);
        result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        try{
            result = this.paramsNotNullReturn(result,userId);
            if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(result.getCode())){
                return result;
            }
            BusinessVo<Boolean> businessVo =
                    manageAdminService.updateManagerAdminRole(userId, roleId, status);
            result.setResult(businessVo);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("后台用户修改失败！",e);
        }
        result.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }




    @ApiOperation(value = "用户列表",notes = "用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/manageAdminPage")
    public BaseResultVo<BasePageInfoVo<ManagerAdminVo>> manageAdminPage(
                                                                     @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                     @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("请求子用户列表，参数为：{}","{pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<ManagerAdminVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        baseResultVo = this.paramsNotNullReturn(baseResultVo,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }

        baseResultVo.setResult(manageAdminService.manageAdminPage(pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


}
