package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.Borrower;
import com.xiaochong.loan.background.entity.po.RepaymentSerialRecord;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.RepaymentSerialService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * Created by jinxin on 2017/9/6.
 * 还款流水控制类
 */
@Api(value = "前台还款流水接口")
@RestController
@RequestMapping("/pro/repaymentSerial")
public class RepaymentSerialController {

    private Logger logger = LoggerFactory.getLogger(RepaymentSerialController.class);

    @Autowired
    private RepaymentSerialService repaymentSerialService;


    @ApiOperation(value = "还款流水查询接口", notes = "还款流水查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询状态（1 交易流水号  2 转账账户姓名 3 用户姓名 4 用户手机号 5 用户身份证   ）", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "流水状态 1  已充值 2 带匹配 3 待充值", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/search")
    public BaseResultVo<RepaymentSerialVo> selectLoanOrderList(@RequestParam(value = "token") String token,
                                                               @RequestParam(value = "searchStatus", required = false) String searchStatus,
                                                               @RequestParam(value = "condition", required = false) String condition,
                                                               @RequestParam(value = "status", required = false) String status,
                                                               @RequestParam(value = "pageNum", required = true) Integer pageNum,
                                                               @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        logger.info("还款流水查询接口，searchStatus：{}，condition：{},status:{}", searchStatus, condition, status);
        BaseResultVo<RepaymentSerialVo> baseResultVo = new BaseResultVo<>();
        try {
            if(JudgeBlankUtils.JudgeBlank(pageNum,pageSize)){
                RepaymentSerialRecord repaymentSerialRecord = new RepaymentSerialRecord();
                Borrower borrower = new Borrower();
                if (StringUtils.isNotBlank(searchStatus) && StringUtils.isNotBlank(condition)) {
                    if ("1".equals(searchStatus)) {
                        repaymentSerialRecord.setDealSerialNo(condition);
                        borrower=null;
                    } else if ("2".equals(searchStatus)) {
                        repaymentSerialRecord.setTransferAccountName(condition);
                        borrower=null;
                    }else if ("3".equals(searchStatus)) {
                        borrower.setName(condition);
                    }else if ("4".equals(searchStatus)) {
                        borrower.setPhone(condition);
                    }else if ("5".equals(searchStatus)) {
                        borrower.setIdCard(condition);
                    }
                }
                if(StringUtils.isBlank(condition)){
                    borrower=null;
                }
                BusinessVo<RepaymentSerialVo> businessVo = repaymentSerialService.selectByRepaymentSerialRecord(repaymentSerialRecord,borrower,status, pageNum, pageSize, token);
                baseResultVo.setResult(businessVo);
                baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
            }else {
                baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            }
        } catch (Exception e) {
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("放款订单查询失败，异常栈：{}，", e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "充值全部待充值", notes = "充值全部待充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/rechargeAllWaitingRecharge")
    public BaseResultVo<String> repaymentAll(@RequestParam(value = "token", required = true) String token) {
        BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("充值全部待充值：token：{}", token);
        try {
            BusinessVo<String> businessVo = repaymentSerialService.rechargeAll(token);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询还款记录页面还款失败！", e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "充值本页待充值或充值指定id", notes = "充值本页待充值或充值指定id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "ids", value = "还款流水的ids，用逗号隔开  例： 1,2,3 ", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/rechargeByIds")
    public BaseResultVo<String> repaymentByIds(@RequestParam(value = "token", required = true) String token,
                                               @RequestParam(value = "ids", required = true) String ids
    ) {
        BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("充值本页待充值或充值指定id：token：{},ids:{}", token, ids);
        try {
            BusinessVo<String> businessVo = repaymentSerialService.rechargeByIds(token, ids);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询还款记录页面还款失败！", e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "匹配到用户或取消匹配", notes = "匹配到用户或取消匹配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "serialId", value = "还款流水的id ", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "borrowerId", value = "借款人id ", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "type", value = "匹配类型   1 phone 2 idCard ", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "匹配状态  1 匹配  0 取消匹配 ", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/matchBorrower")
    public BaseResultVo<String> matchOrCancelBorrower(@RequestParam(value = "token", required = true) String token,
                                                      @RequestParam(value = "serialId", required = true) Integer serialId,
                                                      @RequestParam(value = "borrowerId", required = true) Integer borrowerId,
                                                      @RequestParam(value = "status", required = true) String status,
                                                      @RequestParam(value = "type", required = true) String type
    ) {
        BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("匹配到用户或取消匹配：token：{},serialId:{},borrowerId:{},status:{},type:{}", token, serialId, borrowerId, status, type);
        try {
            if(JudgeBlankUtils.JudgeBlank(serialId,borrowerId,status,type)){
                BusinessVo<String> businessVo = repaymentSerialService.matchOrCancelBorrower(token, serialId, borrowerId, status,type);
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
            logger.error("匹配到用户或取消匹配失败！", e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "根据手机号或者身份证号查询借款人", notes = "根据手机号或者身份证号查询借款人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "搜索状态 1 phone 2 idCard ", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchContent", value = "搜索条件 ", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/searchBorrower")
    public BaseResultVo<BorrowerVo> searchBorrower(@RequestParam(value = "token", required = true) String token,
                                                        @RequestParam(value = "searchStatus", required = true) String searchStatus,
                                                        @RequestParam(value = "searchContent", required = true) String searchContent
    ) {
        BaseResultVo<BorrowerVo> result = new BaseResultVo<>();
        logger.info("根据手机号或者身份证号查询借款人：token：{},searchStatus:{},searchContent:{}", token, searchStatus, searchContent);
        try {
            BusinessVo<BorrowerVo> businessVo = repaymentSerialService.searchBorrower(token, searchStatus, searchContent);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("根据手机号或者身份证号查询借款人失败！", e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "添加还款流水", notes = "添加还款流水")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号码 ", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "transferMoney", value = "还款金额 ", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "dealTime", value = "还款时间 yyyy-MM-dd", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "transferMark", value = "还款备注 ", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/addRepaymentSerial")
    public BaseResultVo<String> addRepaymentSerial(@RequestParam(value = "token", required = true) String token,
                                                        @RequestParam(value = "phone", required = true) String phone,
                                                        @RequestParam(value = "transferMoney", required = true) BigDecimal transferMoney,
                                                        @RequestParam(value = "dealTime", required = true) String dealTime,
                                                        @RequestParam(value = "transferMark", required = true) String transferMark
    ) {
        BaseResultVo<String> result = new BaseResultVo<>();
        logger.info("添加还款流水：token：{},phone:{},transferMoney:{},dealTime:{},transferMark:{}", token, phone, transferMoney, dealTime, transferMark);
        try {
            if(JudgeBlankUtils.JudgeBlank(phone,transferMark,transferMoney,dealTime)){
                RepaymentSerialRecord repaymentSerialRecord = new RepaymentSerialRecord();
                repaymentSerialRecord.setDealTime(DateUtils.stringToDate(dealTime,DateUtils.yyyyMMdd_format));
                repaymentSerialRecord.setTransferMark(transferMark);
                repaymentSerialRecord.setTransferMoney(transferMoney);
                BusinessVo<String> businessVo = repaymentSerialService.addRepaymentSerial(token, repaymentSerialRecord,phone);
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
            logger.error("添加还款流水失败！", e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "下载模板", notes = "下载模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
    })
    @GetMapping(value="/downLoadDemo",produces="application/octet-stream")
    public  byte[] downLoadDemo(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "token") String token )throws IOException {
        logger.info("下载模板接口");
        String filename="支付宝还款流水导入模板";
        if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
        }
        response.addHeader("Content-Disposition", "inline;filename=\""+filename+".csv"+"\"");
        return repaymentSerialService.downLoadDemo();
    }

    @ApiOperation(value = "批量导入流水", notes = "批量导入流水")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "file", value = "数据文件", required = true, dataType = "file", paramType = "form"),
    })
    @PostMapping("/leadInbatchSerial")
    public BaseResultVo<String> leadInbatchSerial(@RequestParam(value = "token", required = true) String token,
                                                   @RequestParam(value = "file",required = true) MultipartFile file
    ) {
        BaseResultVo<String> result = new BaseResultVo<>();
        try {
            if(file!=null){
                logger.info("批量导入流水：token：{},fileName:{}", token,file.getOriginalFilename());
                BusinessVo<String> businessVo = repaymentSerialService.leadInbatchSerial(token, file);
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
            logger.error("批量导入流水失败！", e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }


}
