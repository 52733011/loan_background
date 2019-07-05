package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.SmaSendCountVo;
import com.xiaochong.loan.background.entity.vo.SmaTemplateVo;
import com.xiaochong.loan.background.service.SmsTemplateService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
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

/**
 * Created by wujiaxing on 2017/8/10.
 *
 */
@Api(value = "短信接口")
@RestController
@RequestMapping("/smsTemplate")
public class SmsTemplateController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(SmsTemplateController.class);

    @Resource(name = "smsTemplateService")
    private SmsTemplateService smsTemplateService;

    @ApiOperation(value = "新增短信模板", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "模板内容", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "sendStatus", value = "触发方式", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/insertSmsTemplate")
    public BaseResultVo<Boolean> insertSmsTemplate(@RequestParam(value = "token") String token,
                                        @RequestParam(value = "content") String content,
                                        @RequestParam(value = "sendStatus") String sendStatus,
                                        @RequestParam(value = "status") String status){
        logger.info("新增短信模板请求参数:{}","{content:"+content+"}");
        BaseResultVo<Boolean> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,sendStatus);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }
        baseResultVo.setResult(smsTemplateService.insertSmsTemplate(token,content,sendStatus,status));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "修改短信模板", notes = " ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "模板内容", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态 0禁用 1启用", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateSmsTemplate")
    public BaseResultVo<Boolean> updateSmsTemplate(@RequestParam(value = "token") String token,
                                                   @RequestParam(value = "id") Integer id,
                                                   @RequestParam(required = false,value = "content") String content,
                                                   @RequestParam(required = false,value = "status") String status){
        logger.info("修改短信模板请求参数:{}","{content:"+content+"}");
        BaseResultVo<Boolean> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,id);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }
        baseResultVo.setResult(smsTemplateService.updateSmsTemplate(token,id,content,status));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "短信模板列表",notes = "短信模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/smsTemplatePage")
    public BaseResultVo<BasePageInfoVo<SmaTemplateVo>> smsTemplatePage(@RequestParam(value = "token") String token,
                                                                       @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                       @RequestParam(value = "pageSize",required = false)Integer pageSize){
        logger.info("请求短信模板列表，参数为：{}","{token:"+token+"}");
        BaseResultVo<BasePageInfoVo<SmaTemplateVo>> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        baseResultVo = this.paramsNotNullReturn(baseResultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }
        baseResultVo.setResult(smsTemplateService.smsTemplatePage(token,pageNum, pageSize));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("请求短信模板列表结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }



    @ApiOperation(value = "获取发送短信次数",notes = "获取发送短信次数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/getSmaSendCount")
    public BaseResultVo<SmaSendCountVo> getSmaSendCount(@RequestParam(value = "token") String token){
        logger.info("获取发送短信次数，参数为：{}","{token:"+token+"}");
        BaseResultVo<SmaSendCountVo> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        baseResultVo = this.paramsNotNullReturn(baseResultVo,token);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }
        baseResultVo.setResult(smsTemplateService.getSmaSendCount(token));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("获取发送短信次数结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }




    @ApiOperation(value = "获取发送短信示例",notes = "获取发送短信示例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "模板内容", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/getSample")
    public BaseResultVo<String> getSample(@RequestParam(value = "token") String token,
                                          @RequestParam(value = "content") String content){
        logger.info("获取发送短信示例，参数为：{}","{token:"+token+"}");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
        baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        baseResultVo = this.paramsNotNullReturn(baseResultVo,token,content);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(baseResultVo.getCode())){
            return baseResultVo;
        }
        baseResultVo.setResult(smsTemplateService.getSample(token,content));
        baseResultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        logger.info("获取发送短信示例结束，结果为：{}",baseResultVo);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


}
