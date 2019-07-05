package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.entity.po.MerchantAuditData;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.LoanApplicationAuditService;
import com.xiaochong.loan.background.service.SubmitDataPageService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
import com.xiaochong.loan.background.utils.enums.LoanAuditTypeEnum;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/9/6.
 * 放款订单控制类
 */
@Api(value = "前台放款订单审核接口")
@RestController
@RequestMapping("/pro/loanApplicationAudit")
public class LoanApplicationAuditController {

    private Logger logger = LoggerFactory.getLogger(LoanApplicationAuditController.class);

    @Autowired
    private SubmitDataPageService submitDataPageService;

    @Autowired
    private LoanApplicationAuditService loanApplicationAuditService;

    @ApiOperation(value = "待审核订单查询接口", notes = "放款订单查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询状态（1 phone  2 name  3 idcard）", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间（根据房贷申请创建时间），格式：yyyy-MM-dd", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 格式：yyyy-MM-dd", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectLoanOrderList")
    public BaseResultVo<BasePageInfoVo<LoanApplicationVo>> selectLoanOrderList(@RequestParam(value = "token") String token,
                                                                               @RequestParam(value = "searchStatus",required = false) String searchStatus,
                                                                               @RequestParam(value = "condition",required = false) String condition,
                                                                               @RequestParam(value = "beginTime",required = false) String beginTime,
                                                                                     @RequestParam(value = "pageNum",required = true) Integer pageNum,
                                                                                     @RequestParam(value = "pageSize",required = true) Integer pageSize,
                                                                                     @RequestParam(value = "endTime",required = false) String endTime){
        logger.info("待审核订单查询接口，searchStatus：{}，condition：{},beginTime:{},endTime:{}",searchStatus,condition, beginTime,endTime);
        BaseResultVo<BasePageInfoVo<LoanApplicationVo>> baseResultVo = new BaseResultVo<>();
        try {
            LoanApplication loanApplication = new LoanApplication();
            loanApplication.setBeginTime(DateUtils.stringToDate(beginTime,DateUtils.yyyyMMdd_format));
            loanApplication.setEndTime(DateUtils.stringToDate(endTime,DateUtils.yyyyMMdd_format));
            if(StringUtils.isNotBlank(searchStatus)&&StringUtils.isNotBlank(condition)){
                if("1".equals(searchStatus)){
                    loanApplication.setBorrowerPhone(condition);
                }else if("2".equals(searchStatus)){
                    loanApplication.setBorrowerName(condition);
                }else {
                    loanApplication.setBorrowerIdCard(condition);
                }
            }
            List<String> statusList = new ArrayList<>();
            statusList.add(LoanApplicationStatusEnum.WAITING_AUDIT.getType());
            statusList.add(LoanApplicationStatusEnum.LOANED.getType());
            loanApplication.setStatusList(statusList);
            loanApplication.setAuditType(LoanAuditTypeEnum.MERCHANT_AUDIT.getType());
            BusinessVo<BasePageInfoVo<LoanApplicationVo>> businessVo=submitDataPageService.selectLoanApplicationList(loanApplication,pageNum,pageSize,token);
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

    @ApiOperation(value = "下载全部文件接口", notes = "下载全部文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id",required=true, dataType = "int", paramType = "form"),
    })
    @GetMapping(value="/downloadAllFiles",produces="application/octet-stream")
    public  byte[] downloadAllFiles(HttpServletResponse response, @RequestParam(value = "token") String token,
                                                                            @RequestParam(value = "applicationId") Integer applicationId) throws IOException {
        logger.info("下载全部文件接口，applicationId：{}");
        response.addHeader("Content-Disposition", "inline;filename=\"auditFile"+""+".zip"+"\"");
        return submitDataPageService.downloadAllFiles(applicationId);
    }


    @ApiOperation(value = "查询所有审核资料接口", notes = "查询所有待上传资料(查询模板项目)接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectAuditData")
    public BaseResultVo<SubmitDataPageVo> selectAuditData(@RequestParam(value = "token") String token,
                                                         @RequestParam(value = "applicationId") Integer applicationId){
        logger.info("查询所有审核资料接口，applicationId：{}",applicationId);
        BaseResultVo<SubmitDataPageVo> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<SubmitDataPageVo> businessVo=submitDataPageService.selectProjects(applicationId);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询所有审核资料接口失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

     @ApiOperation(value = "确认已放款或重新提交资料接口", notes = "确认已放款或重新提交资料接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "审核状态  1 确认放款 0 重新提交", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "auditContent", value = "审核内容", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/auditOrResubmit")
    public BaseResultVo<String> auditOrResubmit(@RequestParam(value = "token") String token,
                                                         @RequestParam(value = "applicationId") Integer applicationId,
                                                         @RequestParam(value = "status") String status,
                                                         @RequestParam(value = "auditContent") String auditContent){
        logger.info("确认已放款或重新提交资料接口，applicationId：{},status:{}",applicationId,status);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            MerchantAuditData merchantAuditData = new MerchantAuditData();
            merchantAuditData.setAuditContent(auditContent);
            merchantAuditData.setApplicationId(applicationId);
            merchantAuditData.setStatus(status);
            BusinessVo<String> businessVo=loanApplicationAuditService.auditOrResubmit(merchantAuditData,token, LoanAuditTypeEnum.MERCHANT_AUDIT.getType());
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("确认已放款或重新提交资料失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
         LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "还款计划查询接口", notes = "还款计划查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectRepayPlan")
    public BaseResultVo<BasePageInfoVo<RepaymentPlanVo>> selectRepayPlan(@RequestParam(value = "token") String token,
                                                         @RequestParam(value = "applicationId") Integer applicationId,
                                                         @RequestParam(value = "pageNum") Integer pageNum,
                                                         @RequestParam(value = "pageSize") Integer pageSize){
        logger.info("还款计划查询接口，applicationId：{}",applicationId);
        BaseResultVo<BasePageInfoVo<RepaymentPlanVo>> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<BasePageInfoVo<RepaymentPlanVo>> businessVo=loanApplicationAuditService.selectRepayPlan(applicationId,pageNum,pageSize);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("确认已放款或重新提交资料失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "账户余额查看接口", notes = "账户余额查看接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectBorrowerAccount")
    public BaseResultVo<BorrowerBalanceVo> selectBorrowerAccount(@RequestParam(value = "token") String token,
                                                                         @RequestParam(value = "applicationId") Integer applicationId){
        logger.info("账户余额查看接口，applicationId：{}",applicationId);
        BaseResultVo<BorrowerBalanceVo> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<BorrowerBalanceVo> businessVo=loanApplicationAuditService.selectBorrowerAccount(applicationId);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("账户余额查看接口失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

}
