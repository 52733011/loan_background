package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.LoanApplicationVo;
import com.xiaochong.loan.background.service.RepaymentRecordService;
import com.xiaochong.loan.background.service.SubmitDataPageService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinxin on 2017/9/6.
 * 放款订单控制类
 */
@Api(value = "前台查询放款订单接口")
@RestController
@RequestMapping("/pro/loanOrders")
public class LoanOrdersController {

    private Logger logger = LoggerFactory.getLogger(LoanOrdersController.class);

    @Autowired
    private SubmitDataPageService submitDataPageService;

    @Autowired
    private RepaymentRecordService repaymentRecordService;

    @ApiOperation(value = "放款订单查询接口", notes = "放款订单查询接口")
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
        logger.info("放款订单查询接口，searchStatus：{}，condition：{},beginTime:{},endTime:{}",searchStatus,condition, beginTime,endTime);

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
            statusList.add(LoanApplicationStatusEnum.LOANED.getType());
            loanApplication.setStatusList(statusList);
//            loanApplication.setAuditType(LoanAuditTypeEnum.MERCHANT_AUDIT.getType());
            BusinessVo<BasePageInfoVo<LoanApplicationVo>> businessVo=submitDataPageService.selectLoanApplicationList(loanApplication,pageNum,pageSize, token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("放款订单查询失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "放款查看页面还款接口",notes = "放款查看页面还款接口")
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
        logger.info("放款查看页面还款接口：token：{}，repaymentPlanId：{}，reductiveMoney：{}",token,repaymentPlanId,reductiveMoney);
        try {
            if(reductiveMoney==null){
                reductiveMoney=BigDecimal.ZERO;
            }
            BusinessVo<String> businessVo = repaymentRecordService.repayment(token,repaymentPlanId,reductiveMoney);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("放款查看页面还款失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }



}
