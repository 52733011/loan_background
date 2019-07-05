package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.LoanApplicationService;
import com.xiaochong.loan.background.utils.BigDecimalOperationUtils;
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
import java.util.List;

/**
 * Created by jinxin on 2017/9/11.
 */
@Api(value = "前台放款订单生成接口")
@RestController
@RequestMapping("/pro/loanApplication")
public class LoanApplicationController {

    private Logger logger = LoggerFactory.getLogger(LoanApplicationController.class);

    @Autowired
    private LoanApplicationService loanApplicationService;

    @ApiOperation(value = "根据订单号查询个人信息接口", notes = "根据订单号查询个人信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/selectBorrowerInfoByOrderNo")
    public BaseResultVo<UserInfoVo> selectLoanOrderList(@RequestParam(value = "token") String token,
                                                                        @RequestParam(value = "orderNo",required = false) String orderNo){
        logger.info("根据订单号查询个人信息接口，orderNo：{}",orderNo);
        BaseResultVo<UserInfoVo> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<UserInfoVo> businessVo=loanApplicationService.selectBorrowerInfoByOrderNo(orderNo,token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("根据订单号查询个人信息接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "根据放款类型查询费率列表", notes = "根据放款类型查询费率列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "rateType", value = "放贷类型 0 按月 1 按日", required = false, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectRateTemplateByType")
    public BaseResultVo<List<RateTemplateVo>> selectRateTemplateByType(@RequestParam(value = "token") String token,
                                                                      @RequestParam(value = "rateType",required = false) Integer rateType){
        logger.info("根据放款类型查询费率列表，rateType：{}",rateType);
        BaseResultVo<List<RateTemplateVo>> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<List<RateTemplateVo>> businessVo=loanApplicationService.selectRateTemplateByType(rateType,token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("待审核订单查询失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "查询商户数据模板接口", notes = "根据放款类型查询费率列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/selectDataTemplateByMerchId")
    public BaseResultVo<List<MerchDataTemplateVo>> selectDataTemplateByMerchId(@RequestParam(value = "token") String token){
        logger.info("查询商户数据模板接口");

        BaseResultVo<List<MerchDataTemplateVo>> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<List<MerchDataTemplateVo>> businessVo=loanApplicationService.selectDataTemplateByMerchId(token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询商户数据模板接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "发起放款申请接口", notes = "发起放款申请接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "loanMoney", value = "放款金额", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "stageType", value = "分期方式 0,按月放款 1,按日放款", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "rateId", value = "费率id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "stageLimit", value = "分期期限", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "templateId", value = "数据模板id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "submitType", value = "数据提交方式 1 网页 2 手机  提交方式为手机时 数据提交人类型必输 ", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "submitByType", value = "数据提交人类型  0 被调查人  1 公司人员 为被调查人时 提交人id不必输", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "submitBy", value = "提交人id ", required = false, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/sponsorLoanApplication")
    public BaseResultVo<String> sponsorLoanApplication(@RequestParam(value = "token") String token,
                                                       @RequestParam(value = "orderNo") String orderNo,
                                                       @RequestParam(value = "loanMoney") BigDecimal loanMoney,
                                                       @RequestParam(value = "stageType") String stageType,
                                                       @RequestParam(value = "rateId") Integer rateId,
                                                       @RequestParam(value = "stageLimit") Integer stageLimit,
                                                       @RequestParam(value = "templateId") Integer templateId,
                                                       @RequestParam(value = "submitType",required = true) String submitType,
                                                       @RequestParam(value = "submitByType",required = false) String submitByType,
                                                       @RequestParam(value = "submitBy",required = false) Integer submitBy

    ){
        logger.info("查询商户数据模板接口");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            LoanApplication loanApplication = new LoanApplication();
            loanApplication.setOrderNo(orderNo);
            loanApplication.setLoanMoney(loanMoney);
            loanApplication.setStageType(stageType);
            loanApplication.setRateId(rateId);
            loanApplication.setStageLimit(stageLimit);
            loanApplication.setTemplateId(templateId);
            loanApplication.setSubmitType(submitType);
            loanApplication.setSubmitByType(submitByType);
            loanApplication.setSubmitBy(submitBy);
            BusinessVo<String> businessVo=loanApplicationService.sponsorLoanApplication(loanApplication,token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询商户数据模板接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

}
