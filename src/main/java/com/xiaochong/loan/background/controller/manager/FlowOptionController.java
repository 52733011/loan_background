package com.xiaochong.loan.background.controller.manager;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.Field;
import com.xiaochong.loan.background.entity.po.FlowOption;
import com.xiaochong.loan.background.entity.po.MerchantFlowOption;
import com.xiaochong.loan.background.entity.po.MerchantinfoFlow;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.FieldService;
import com.xiaochong.loan.background.service.FlowOptionService;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;
import java.util.List;

/**
 * Created by jinxin on 2017/8/12.
 * 认证流程项
 */
@Api(value = "后台商户认证流程项管理")
@RestController
@RequestMapping("/back/flowOption")
public class FlowOptionController {


    private Logger logger = LoggerFactory.getLogger(FlowOptionController.class);

    @Autowired
    private FlowOptionService flowOptionService;

    @ApiOperation(value = "查询认证流程下所有认证项",notes = "查询认证流程下所有认证项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flowNo", value = "认证号",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/selectByFlowNo")
    public BaseResultVo<List<FlowOptionVo>> selectByFlowNoAndMerchId(@RequestParam("flowNo")String flowNo) {
        logger.info("查询认证流程下所有认证项：flowNo:{}",flowNo);
        BaseResultVo<List<FlowOptionVo>> result = new BaseResultVo<>();
        try {
            BusinessVo<List<FlowOptionVo>> businessVo=flowOptionService.selectByFlowNo(flowNo);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询认证流程下所有认证项失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "查询该商户认证流程下所有认证项",notes = "查询该商户认证流程下所有认证项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flowNo", value = "认证号",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "merchId", value = "商户号",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
    @PostMapping("/selectByFlowNoAndMerchId")
    public BaseResultVo<MerchantFlowOptionVo> selectByFlowNo(@RequestParam("flowNo")String flowNo,
                                                           @RequestParam("merchId")Integer merchId) {
        logger.info("查询认证流程下所有认证项：flowNo:{},merchId:{}",flowNo,merchId);
        BaseResultVo<MerchantFlowOptionVo> result = new BaseResultVo<>();
        try {
            MerchantFlowOption merchantinfoFlow = new MerchantFlowOption();
            merchantinfoFlow.setMerchId(merchId);
            merchantinfoFlow.setFlowNo(flowNo);
            BusinessVo<MerchantFlowOptionVo> businessVo=flowOptionService.selectByFlowNoAndMerchid(merchantinfoFlow);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询认证流程下所有认证项失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "添加或者修改商户认证流程下的认证流程项",notes = "添加或者修改商户认证流程下的认证流程项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchId", value = "商户号",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "flowNo", value = "认证流程号",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "flowOption", value = "认证流程项，多个以英文逗号隔开",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")

    })
    @PostMapping("/insertOrUpdateFlowOption")
    public BaseResultVo<String> insertOrUpdateFlowOption(@RequestParam("merchId")Integer merchId,
                                                         @RequestParam("flowNo")String flowNo,
                                                         @RequestParam("flowOption")String flowOption) {
        logger.info("添加或者修改商户认证流程下的认证流程项：flowNo:{},merchId:{},flowOption:{}",flowNo,merchId,flowOption);
        BaseResultVo<String> result = new BaseResultVo<>();
        try {
            MerchantFlowOption merchantFlowOption = new MerchantFlowOption();
            merchantFlowOption.setMerchId(merchId);
            merchantFlowOption.setFlowNo(flowNo);
            BusinessVo<String> businessVo=flowOptionService.insertOrUpdateFlowOption(merchantFlowOption,flowOption);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询认证流程下所有认证项失败！",e);
        }
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
        return result;
    }

}
