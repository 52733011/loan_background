package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.AccountRecord;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.AccountRecordService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by jinxin on 2017/8/15.
 */
@Api(value = "前台商户账户记录管理接口")
@RestController
@RequestMapping("/pro/accountrecord")
public class AccountRecordController {

    private Logger logger = LoggerFactory.getLogger(AccountRecordController.class);

    @Autowired
    private AccountRecordService accountRecordService;

    @ApiOperation(value = "查询所有账户变动信息",notes = "查询所有账户变动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "timeType", value = "时间类型：三个月之前 1  近三个月 0 ",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "订单号",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "transactType", value = "交易类型",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form")
    })
    @PostMapping("/search")
    public BaseResultVo<BasePageInfoVo<AccountRecordVo>> search(@RequestParam(value = "orderNo",required = false)String orderNo,
                                                                @RequestParam(value = "timeType",required = false)Integer timeType,
                                                                @RequestParam(value = "transactType",required = false)String transactType,
                                                                @RequestParam(value = "orderStatus",required = false)String orderStatus,
                                                                @RequestParam(value = "beginTime",required = false)String beginTime,
                                                                @RequestParam(value = "endTime",required = false)String endTime,
                                                                @RequestParam(value = "token",required = true)String token,
                                                                @RequestParam(value = "pageNum",required = true)Integer pageNum,
                                                                @RequestParam(value = "pageSize",required = true)Integer pageSize){
        BaseResultVo<BasePageInfoVo<AccountRecordVo>> result = new BaseResultVo<>();
        logger.info("查询所有账户变动信息：token：{}，orderNo：{}，transactType：{}，orderStatus：{}",token,orderNo,transactType,orderStatus);
        try {
            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setOrderNo(orderNo);
            accountRecord.setTransactType(transactType);
            Date date = DateUtils.addMonth(new Date(), -3);
            if(timeType!=null&&timeType==1){
                accountRecord.setEndTime(date);
            }else  {
                accountRecord.setBeginTime(date);
            }
            accountRecord.setOrderStatus(orderStatus);
            BusinessVo<BasePageInfoVo<AccountRecordVo>> businessVo = accountRecordService.selectAccountRecord(accountRecord,token,pageNum,pageSize);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询账户信息失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "前台账户余额信息",notes = "前台账户余额信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getAccountInfo")
    public BaseResultVo<MerchantAccountVo> getAccountInfo(@RequestParam(value = "token",required = false)String token){
        BaseResultVo<MerchantAccountVo> result = new BaseResultVo<>();
        logger.info("查询所有账户变动信息：token：{}",token);
        try {
            BusinessVo<MerchantAccountVo> businessVo = accountRecordService.getAccountInfo(token);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询账户余额信息失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response",JSON.toJSONString(result));
        return result;
    }

}
