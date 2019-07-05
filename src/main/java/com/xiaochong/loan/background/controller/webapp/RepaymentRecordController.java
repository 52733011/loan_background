package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.AccountRecord;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.AccountRecordService;
import com.xiaochong.loan.background.service.RepaymentRecordService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.JudgeBlankUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by jinxin on 2017/8/15.
 * 还款记录控制类
 */
@Api(value = "前台还款记录接口")
@RestController
@RequestMapping("/pro/repaymentRecord")
public class RepaymentRecordController {

    private Logger logger = LoggerFactory.getLogger(RepaymentRecordController.class);

    @Autowired
    private RepaymentRecordService repaymentRecordService;

    @ApiOperation(value = "根据条件查询还款记录",notes = "根据条件查询还款记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = " 1 待还款  2 提前还款  4 逾期还款",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "搜索类型 1 手机号  2  身份证号",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchContent", value = "搜索内容",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/search")
    public BaseResultVo<RepaymentRecordVo> search(@RequestParam(value = "token",required = true)String token,
                                                                @RequestParam(value = "status",required = false)String status,
                                                                @RequestParam(value = "searchStatus",required = false)String searchStatus,
                                                                @RequestParam(value = "searchContent",required = false)String searchContent,
                                                                @RequestParam(value = "pageNum",required = true)Integer pageNum,
                                                                @RequestParam(value = "pageSize",required = true)Integer pageSize){
        BaseResultVo<RepaymentRecordVo> result = new BaseResultVo<>();
        logger.info("查询还款记录：token：{}，status：{}，searchStatus：{}，searchContent：{}",token,status,searchStatus,searchContent);
        try {
            if(JudgeBlankUtils.JudgeBlank(pageNum,pageSize)){
                BusinessVo<RepaymentRecordVo> businessVo = repaymentRecordService.searchRepaymentRecord(token,status,searchStatus,searchContent,pageNum,pageSize);
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
            logger.error("查询还款记录失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

  @ApiOperation(value = "查询还款记录页面还款接口",notes = "查询还款记录页面还款接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "repaymentPlanId", value = " 还款计划id",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "reductiveMoney", value = " 减免金额",required=false, dataType = "BigDecimal", paramType = "form"),
    })
    @PostMapping("/repayment")
    public BaseResultVo<String> repayment(@RequestParam(value = "token",required = true)String token,
                                                                @RequestParam(value = "repaymentPlanId",required = true)Integer repaymentPlanId,
                                                                @RequestParam(value = "reductiveMoney",required = false)BigDecimal reductiveMoney){
        BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("查询还款记录页面还款接口：token：{}，repaymentPlanId：{}，reductiveMoney：{}",token,repaymentPlanId,reductiveMoney);
        try {
            if(JudgeBlankUtils.JudgeBlank(repaymentPlanId)){
                if(reductiveMoney==null){
                    reductiveMoney=BigDecimal.ZERO;
                }
                BusinessVo<String> businessVo = repaymentRecordService.repayment(token,repaymentPlanId,reductiveMoney);
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
            logger.error("查询还款记录页面还款失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
      LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }



}
