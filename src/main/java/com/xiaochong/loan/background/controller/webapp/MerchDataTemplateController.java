package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.MerchDataTemplate;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.MerchDataTemplateService;
import com.xiaochong.loan.background.utils.DateUtils;
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

/**
 * Created by jinxin on 2017/9/1.
 */
@Api(value = "前台数据模板接口")
@RestController
@RequestMapping("/pro/merchDataTemplate")
public class MerchDataTemplateController {

    private Logger logger = LoggerFactory.getLogger(MerchDataTemplateController.class);

    @Autowired
    private MerchDataTemplateService merchDataTemplateService;


    @ApiOperation(value = "商户模板分页查询", notes = "商户模板分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/search")
    public BaseResultVo<BasePageInfoVo<MerchDataTemplateVo>> startBd(@RequestParam(value = "token") String token,
                                                                    @RequestParam(value = "pageSize") Integer pageSize,
                                                                    @RequestParam(value = "pageNum") Integer pageNum){
        logger.info("商户模板分页查询pageSize：{}，pageNum：{}",pageSize,pageNum);
        BaseResultVo<BasePageInfoVo<MerchDataTemplateVo>> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<BasePageInfoVo<MerchDataTemplateVo>> businessVo = merchDataTemplateService.merchDataTemplateListSearch(token, pageSize, pageNum);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("商户模板分页查询失败",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "商户模板添加", notes = "商户模板添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "templateName", value = "模板名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "projectList", value = "项目集合[{projectName:value,fileType:value,mark:value},{}],项目文件类型 1  图片资料  2 文件资料 3 视频资料  4 文字输入", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "templateDesc", value = "模板描述", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/insert")
    public BaseResultVo<String> startBd(@RequestParam(value = "token") String token,
                                                                     @RequestParam(value = "templateName") String templateName,
                                                                     @RequestParam(value = "projectList") String projectList,
                                                                     @RequestParam(value = "templateDesc") String templateDesc){
        logger.info("商户模板添加，templateName：{}，projectList：{}，templateDesc：{}",templateName,projectList,templateDesc);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            MerchDataTemplate merchDataTemplate = new MerchDataTemplate();
            merchDataTemplate.setTemplateName(templateName);
            merchDataTemplate.setTemplateDesc(templateDesc);
            BusinessVo<String> businessVo = merchDataTemplateService.insertMerchDataTemplate(token,merchDataTemplate,projectList);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("商户模板添加失败",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "商户模板状态更改", notes = "商户模板状态更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "templateId", value = "模板id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态  1启用  0 停用", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/updateStatus")
    public BaseResultVo<String> updateStatus(@RequestParam(value = "token") String token,
                                                                     @RequestParam(value = "templateId") Integer templateId,
                                                                     @RequestParam(value = "status") String status){
        logger.info("商户模板添加，templateId：{}，status：{}",templateId,status);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            MerchDataTemplate merchDataTemplate = new MerchDataTemplate();
            merchDataTemplate.setId(templateId);
            merchDataTemplate.setStatus(status);
            BusinessVo<String> businessVo = merchDataTemplateService.updateStatus(merchDataTemplate);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("商户模板添加失败",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

}
