package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.Borrower;
import com.xiaochong.loan.background.entity.po.RepaymentSerialRecord;
import com.xiaochong.loan.background.entity.po.UrgeOverdue;
import com.xiaochong.loan.background.entity.po.UrgeRecord;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.RepaymentRecordService;
import com.xiaochong.loan.background.service.RepaymentSerialService;
import com.xiaochong.loan.background.service.SubmitDataPageService;
import com.xiaochong.loan.background.service.UrgeOverDueService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.JudgeBlankUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by jinxin on 2017/9/6.
 * 还款流水控制类
 */
@Api(value = "前台逾期催收接口")
@RestController
@RequestMapping("/pro/overDue")
public class UrgeOverDueController {

    private Logger logger = LoggerFactory.getLogger(UrgeOverDueController.class);

    @Autowired
    private UrgeOverDueService urgeOverDueService;

    @Autowired
    private SubmitDataPageService submitDataPageService;

    @Autowired
    private RepaymentRecordService repaymentRecordService;

    @ApiOperation(value = "逾期催收查询接口", notes = "逾期催收查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询状态（ 1 用户姓名 2 用户手机号）", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态  1 今日已跟进 2 待跟进  3  已完成", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/search")
    public BaseResultVo<UrgeOverDueSearchVo> selectLoanOrderList(@RequestParam(value = "token") String token,
                                                               @RequestParam(value = "searchStatus", required = false) String searchStatus,
                                                               @RequestParam(value = "condition", required = false) String condition,
                                                               @RequestParam(value = "status", required = false) String status,
                                                               @RequestParam(value = "pageNum", required = true) Integer pageNum,
                                                               @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        logger.info("逾期催收查询接口，searchStatus：{}，condition：{},status:{}", searchStatus, condition);
        BaseResultVo<UrgeOverDueSearchVo> baseResultVo = new BaseResultVo<>();

        try {
            UrgeOverdue urgeOverdue = new UrgeOverdue();
            if (StringUtils.isNotBlank(searchStatus) && StringUtils.isNotBlank(condition)) {
                if ("1".equals(searchStatus)) {
                    urgeOverdue.setBorrowerName(condition);
                } else if ("2".equals(searchStatus)) {
                    urgeOverdue.setBorrowerPhone(condition);
                }
            }
            BusinessVo<UrgeOverDueSearchVo> businessVo = urgeOverDueService.selectByUrgeOverdue(urgeOverdue,status, pageNum, pageSize, token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("逾期催收查询失败，异常栈：{}，", e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "催收页面查询所有审核资料接口", notes = "催收页面查询所有审核资料接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectAuditData")
    public BaseResultVo<SubmitDataPageVo> selectAuditData(@RequestParam(value = "token") String token,
                                                          @RequestParam(value = "applicationId") Integer applicationId){
        logger.info("催收页面查询所有审核资料接口，applicationId：{}",applicationId);
        BaseResultVo<SubmitDataPageVo> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<SubmitDataPageVo> businessVo=submitDataPageService.selectProjects(applicationId);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("催收页面查询所有审核资料接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "催记查询分页接口", notes = "催记查询分页接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "urgeOverDueId", value = "催收逾期放款id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小", required = true, dataType = "Integer", paramType = "form"),

    })
    @PostMapping("/selectUrgeRecord")
    public BaseResultVo<BasePageInfoVo<UrgeRecordVo>> selectUrgeRecord(@RequestParam(value = "token") String token,
                                                                     @RequestParam(value = "urgeOverDueId") Integer urgeOverDueId,
                                                                       @RequestParam(value = "pageNum", required = true) Integer pageNum,
                                                                       @RequestParam(value = "pageSize", required = true) Integer pageSize
   ){
        logger.info("催记查询分页接口，urgeOverDueId：{}",urgeOverDueId);
        BaseResultVo<BasePageInfoVo<UrgeRecordVo>> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<BasePageInfoVo<UrgeRecordVo>> businessVo=urgeOverDueService.selectUrgeRecord(urgeOverDueId,pageNum,pageSize);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("催记查询分页接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "新增催记接口", notes = "新增催记接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "urgeOverDueId", value = "逾期催记Id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "urgeRecordContent", value = "催记内容", required = true, dataType = "String", paramType = "form"),

    })
    @PostMapping("/addUrgeRecord")
    public BaseResultVo<String> addUrgeRecord(@RequestParam(value = "token") String token,
                                                                     @RequestParam(value = "urgeOverDueId") Integer urgeOverDueId,
                                                                       @RequestParam(value = "urgeRecordContent", required = true) String urgeRecordContent
   ){
        logger.info("新增催记接口，urgeOverDueId：{}，urgeRecordContent：{}",urgeOverDueId,urgeRecordContent);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            if(JudgeBlankUtils.JudgeBlank(urgeOverDueId,urgeRecordContent)){
                BusinessVo<String> businessVo=urgeOverDueService.addUrgeRecord(token,urgeOverDueId,urgeRecordContent);
                baseResultVo.setResult(businessVo);
                baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
            }else {
                baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            }
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("新增催记接口，异常栈：{}，",e);
        }
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        return baseResultVo;
    }

    @ApiOperation(value = "催记页面还款接口",notes = "催记页面还款接口")
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
        logger.info("催记页面还款接口：token：{}，repaymentPlanId：{}，reductiveMoney：{}",token,repaymentPlanId,reductiveMoney);
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
            logger.error("催记页面还款失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

}
