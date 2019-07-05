package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.OrderWebappVo;
import com.xiaochong.loan.background.service.OrderService;
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
import java.util.List;
import java.util.Map;

/**
 * Created by wujiaxing on 2017/8/10.
 *
 */
@Api(value = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource(name = "sessionComponent")
    private SessionComponent sessionComponent;

    @Resource(name = "orderService")
    private OrderService orderService;

    @ApiOperation(value = "发起背调", notes = "姓名+身份证号+手机号=添加结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "card", value = "身份证号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/startBd")
    public BaseResultVo<String> startBd(@RequestParam(value = "token") String token,
                                        @RequestParam(value = "name") String name,
                                        @RequestParam(value = "card") String card,
                                        @RequestParam(value = "phone") String phone){
        logger.info("发起背调请求参数:{}","{name:"+name+",card:"+card+",phone:"+phone+"traceId"+LogTrace.getTrace().getTraceId()+"}");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(card) &&
                StringUtils.isNotBlank(phone)){
            try {
                baseResultVo.setResult(orderService.startBd(token,name, card, phone));
            } catch (Exception e) {
                baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
                logger.error("发起背调失败！",e);
            }
        }else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "背调订单列表",notes = "背调订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "订单状态:0未授权 1认证中 2报告生成中 3已拒绝 4已取消 5报告完成 6已过期 7异常,(逗号','分隔)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询方式(realname、phone、idCard)", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/orderList")
    public BaseResultVo<BasePageInfoVo<OrderWebappVo>> orderList(@RequestParam(value = "token") String token,
                                                                 @RequestParam(value = "status",required = false)String status,
                                                                 @RequestParam(value = "condition",required = false)String condition,
                                                                 @RequestParam(value = "searchStatus",required = false)String searchStatus,
                                                                 @RequestParam(value = "startTime",required = false)String startTime,
                                                                 @RequestParam(value = "endTime",required = false)String endTime,
                                                                 @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                 @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("请求订单列表，参数为：{}","{condition:"+condition+",startTime:"+startTime+",endTime"+endTime+",pageNum:"+pageNum+",pageSize:"+pageSize+"}");
        BaseResultVo<BasePageInfoVo<OrderWebappVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        if(StringUtils.isBlank(proxyUserId)){
            baseResultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
            baseResultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
            return baseResultVo;
        }else if (pageNum != null && pageSize != null){
            baseResultVo.setResult(orderService.getOrderPage(proxyUserId,status,condition,searchStatus,startTime,endTime,pageNum, pageSize));
        }else {
            baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
            baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            return baseResultVo;
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求订单列表结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "查询各状态订单数量",notes = "查询各状态订单数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/orderStatusNum")
    public BaseResultVo<List<Map<String,Object>>> orderStatusNum(@RequestParam(value = "token") String token){
        logger.info("查询各状态订单数量，参数为：{}","{token:"+token+"}");
        BaseResultVo<List<Map<String,Object>>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        String proxyUserId = sessionComponent.getAttribute(token + "-" + UserLoginTypeEnum.WEBAPP.getType());
        if(StringUtils.isBlank(proxyUserId)){
            baseResultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
            baseResultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
            return baseResultVo;
        }else {
            baseResultVo.setResult(orderService.getOrderStatusNum(proxyUserId));
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("查询各状态订单数量，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

}
