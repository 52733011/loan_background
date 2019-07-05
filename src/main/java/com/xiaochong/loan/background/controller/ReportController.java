package com.xiaochong.loan.background.controller;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.ReportResultVo;
import com.xiaochong.loan.background.service.ReportService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Created by jinxin on 2017/8/18.
 */
@Api(value = "前台报告接口")
@RestController
@RequestMapping("/report")
public class ReportController {

    private Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private SessionComponent sessionComponent;


    @ApiOperation(value = "根据订单号查询背调报告",notes = "根据订单号查询背调报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "requestType", value = "请求类型 1 前台 后台 0", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectByOrderNo")
    public BaseResultVo<ReportResultVo> selectByOrderNo(@RequestParam("orderNo")String orderNo,
                                                  @RequestParam("requestType")Integer requestType,
                                                  @RequestParam("token")String token){
        logger.info("根据订单号查询背调报告:{}","{orderNo:"+orderNo+"}"+"{requestType:"+requestType+"}"+"{token:"+token+"}");
        BaseResultVo<ReportResultVo> baseResultVo = new BaseResultVo<>();
        try{
            if(!checkToken(baseResultVo,requestType,token)){
                logger.info("token失效了：{}",token);
                return baseResultVo;
            }
            BusinessVo<ReportResultVo> businessVo = reportService.selectByOrderNo(orderNo);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.REPORT_QUERY_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.REPORT_QUERY_ERROR_DESC);
            logger.error("根据订单号查询报告失败：{}",e);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        //logger.info("根据订单号查询背调报告返回：{}",baseResultVo);
        return baseResultVo;
    }



    @ApiOperation(value = "根据订单号查询旧版背调报告",notes = "根据订单号查询旧版背调报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/selectOldByOrderNo")
    public BaseResultVo<String> selectOldByOrderNo(@RequestParam("orderNo")String orderNo,
                                                  @RequestParam("token")String token){
        logger.info("根据订单号查询旧版背调报告:{}","{orderNo:"+orderNo+"}"+"{token:"+token+"}");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try{
            BusinessVo<String> businessVo = reportService.selectOldByOrderNo(orderNo);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.REPORT_QUERY_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.REPORT_QUERY_ERROR_DESC);
            logger.error("根据订单号查询旧版背调报告：{}",e);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
//        logger.info("根据订单号查询旧版背调报告返回：{}",baseResultVo);
        return baseResultVo;
    }


    @ApiOperation(value = "根据订单号查询旧版背调报告",notes = "根据订单号查询旧版背调报告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/yys_call_count")
    public BaseResultVo<String> getYysCallCount(@RequestParam("orderNo")String orderNo,
                                                  @RequestParam("token")String token){
        logger.info("根据订单号查询旧版背调报告:{}","{orderNo:"+orderNo+"}"+"{token:"+token+"}");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try{
            BusinessVo<String> businessVo = reportService.getYysCallCount(orderNo);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.REPORT_QUERY_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.REPORT_QUERY_ERROR_DESC);
            logger.error("根据订单号查询旧版背调报告：{}",e);
        }
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
//        logger.info("根据订单号查询旧版背调报告返回：{}",baseResultVo);
        return baseResultVo;
    }

    @ApiOperation(value = "导出pdf背调报告",notes = "成功返回下载链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "orderNo", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "requestType", value = "请求类型 1 前台 后台 0", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/downloadReport")
    public BaseResultVo<String> downloadReport(@RequestParam("orderNo")String orderNo,
                                                               @RequestParam(value = "token")String token,
                                                               @RequestParam(value = "requestType")Integer requestType
    ){
        logger.info("根据订单号导出pdf背调报告:orderNo{}，requestType{}",orderNo,requestType);
        BaseResultVo<String> resultVo = new BaseResultVo<>();
        if(!checkToken(resultVo,requestType,token)){
            return resultVo;
        }
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (Objects.nonNull(orderNo) && StringUtils.isNotBlank(token)){
            BusinessVo<String> reportUrl = reportService.getReportUrl(orderNo, token,requestType);
            resultVo.setResult(reportUrl);
        }else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        logger.info("报告链接：{}",resultVo.getResult().getData());
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }

    /**
     * report控制类不经过拦截器，直接验证token
     * @param baseResultVo
     * @param requestType
     * @param token
     * @return
     */
    private boolean checkToken(BaseResultVo baseResultVo,Integer requestType,String token){
        StringBuilder key=null;
        String id=null;
        ManageAdmin manageAdmin =null;
        if(requestType==1){
            key = new StringBuilder(token).append("-").append(UserLoginTypeEnum.WEBAPP.getType());
            id= sessionComponent.getAttribute(key.toString());
        }else {
            key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
            manageAdmin = sessionComponent.getAttributeManageAdmin(key.toString());
        }
        if(StringUtils.isBlank(id)&&manageAdmin==null){
            baseResultVo.setCode(ResultConstansUtil.LOGIN_INVALID_CODE);
            baseResultVo.setMessage(ResultConstansUtil.LOGIN_INVALID_DESC);
            return false;
        }
        return true;
    }


}
