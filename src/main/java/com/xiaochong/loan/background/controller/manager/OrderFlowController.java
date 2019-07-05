package com.xiaochong.loan.background.controller.manager;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.OrderFlowBackVo;
import com.xiaochong.loan.background.service.OrderFlowService;
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
import java.util.Date;


/**
 * Created by wujiaxing on 2017/8/24.
 * 认证信息
 */
@Api(value = "后台认证信息")
@RestController
@RequestMapping("/back/orderFlow")
public class OrderFlowController {


    private Logger logger = LoggerFactory.getLogger(OrderFlowController.class);

    @Resource(name = "orderFlowService")
    private OrderFlowService orderFlowService;

    @ApiOperation(value = "认证流程列表查询",notes = "认证流程列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "流程状态", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询方式(orderNo)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/orderFlowList")
    public BaseResultVo<BasePageInfoVo<OrderFlowBackVo>> orderFlowList(@RequestParam(value = "status",required = false)Integer status,
                                                                             @RequestParam(value = "condition",required = false)String condition,
                                                                             @RequestParam(value = "searchStatus",required = false)String searchStatus,
                                                                             @RequestParam(value = "startTime",required = false)String startTime,
                                                                             @RequestParam(value = "endTime",required = false)String endTime,
                                                                             @RequestParam(value = "pageNum")Integer pageNum,
                                                                             @RequestParam(value = "pageSize")Integer pageSize){
        logger.info("认证流程列表查询，参数为：{}","{status:"+status+",condition:"+condition+
                ",startTime:"+startTime+",endTime"+endTime+",pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<OrderFlowBackVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (pageNum == null || pageSize == null){
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_ERROR_DESC);
            return baseResultVo;
        }
        baseResultVo.setResult(orderFlowService.getOrderFlowList(
                pageNum, pageSize, status, searchStatus, condition, startTime, endTime));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("认证流程列表查询结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


}
