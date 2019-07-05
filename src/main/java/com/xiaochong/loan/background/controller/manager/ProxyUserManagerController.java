package com.xiaochong.loan.background.controller.manager;


import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.ProxyUserService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 商户账户管理
 * @author lhx
 *
 */
@Api(value = "后台商户账户管理")
@RestController
@RequestMapping("/back/proxyUser")
public class ProxyUserManagerController extends BaseController {

    @Resource(name = "proxyUserService")
    private ProxyUserService proxyUserService;

    @ApiOperation(value = "后台商户账户列表",notes = "后台商户账户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchId", value = "商户id", required = false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询方式(username,email,phone,merchName)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/proxyUserPage")
    public BaseResultVo<BasePageInfoVo<ProxyUserVo>> proxyUserPage(@RequestParam(value = "merchId",required = false)Integer merchId,
                                                                       @RequestParam(value = "condition",required = false)String condition,
                                                                       @RequestParam(value = "searchStatus",required = false)String searchStatus,
                                                                       @RequestParam(value = "startTime",required = false)String startTime,
                                                                       @RequestParam(value = "endTime",required = false)String endTime,
                                                                       @RequestParam(value = "pageNum")Integer pageNum,
                                                                       @RequestParam(value = "pageSize")Integer pageSize){
        BaseResultVo<BasePageInfoVo<ProxyUserVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        baseResultVo = this.paramsNotNullReturn(baseResultVo,pageNum,pageSize);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }
        baseResultVo.setResult(proxyUserService.proxyUserPage(
                pageNum, pageSize, merchId, searchStatus, condition, startTime, endTime));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        //LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "修改子账户", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "merchId", value = "商户id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/changeMerch")
    public BaseResultVo<Boolean> changeMerch(@RequestParam("userId")Integer userId,
                                               @RequestParam("merchId")Integer merchId){
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        resultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        resultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);

        resultVo = this.paramsNotNullReturn(resultVo,userId,merchId);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }

        resultVo.setResult(proxyUserService.changeMerch(userId,merchId));
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        //LogTrace.info("response",resultVo.toString());
        return resultVo;
    }

}
