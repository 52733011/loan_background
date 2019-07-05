package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.SubmitDataPageService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jinxin on 2017/9/4.
 */
@Api(value = "前台提交数据接口")
@RestController
@RequestMapping("/pro/submitDataPage")
public class SubmitDataPageController {

    private Logger logger = LoggerFactory.getLogger(SubmitDataPageController.class);

    @Autowired
    private SubmitDataPageService submitDataPageService;


    @ApiOperation(value = "提交数据查询接口", notes = "提交数据查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchStatus", value = "查询状态（1 phone  2 name  3 idcard）", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "condition", value = "查询条件", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间（根据房贷申请创建时间），格式：yyyy-MM-dd", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 格式：yyyy-MM-dd", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectLoanApplicationList")
    public BaseResultVo<BasePageInfoVo<LoanApplicationVo>> selectLoanApplicationList(@RequestParam(value = "token") String token,
                                                                                     @RequestParam(value = "searchStatus",required = false) String searchStatus,
                                                                                     @RequestParam(value = "condition",required = false) String condition,
                                                                                     @RequestParam(value = "beginTime",required = false) String beginTime,
                                                                                     @RequestParam(value = "pageNum",required = true) Integer pageNum,
                                                                                     @RequestParam(value = "pageSize",required = true) Integer pageSize,
                                                                                     @RequestParam(value = "endTime",required = false) String endTime){
        logger.info("提交数据查询接口，searchStatus：{}，condition：{},beginTime:{},endTime:{}",searchStatus,condition, beginTime,endTime);
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
            statusList.add(LoanApplicationStatusEnum.RESUBMIT.getType());
            statusList.add(LoanApplicationStatusEnum.WAITING_SUBMIT.getType());
            statusList.add(LoanApplicationStatusEnum.REFUSED.getType());
            loanApplication.setStatusList(statusList);
            BusinessVo<BasePageInfoVo<LoanApplicationVo>> businessVo=submitDataPageService.selectLoanApplicationList(loanApplication,pageNum,pageSize, token);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("提交数据查询失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "提交数据页关闭订单接口", notes = "提交数据页关闭订单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/closeApplication")
    public BaseResultVo<String> closeApplication(@RequestParam(value = "token") String token,
                                                                            @RequestParam(value = "applicationId") Integer applicationId){
        logger.info("提交数据页关闭订单接口");
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<String> businessVo=submitDataPageService.closeApplication(token,applicationId);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("提交数据页关闭订单接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "提交数据页上传文件接口", notes = "上传文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "projectId", value = "数据模型项目id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "file", value = "文件 fileType为   1  图片资料  2 文件资料 3 视频资料  时必输", required = false, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "fileType为   4 文字输入  时必输", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/uploadFile")
    public BaseResultVo<String> uploadFile(@RequestParam(value = "token") String token,
                                             @RequestParam(value = "applicationId") Integer applicationId,
                                             @RequestParam(value = "projectId") Integer projectId,
                                             @RequestParam(value = "content",required = false) String content,
                                             @RequestParam(value = "file",required = false) MultipartFile file){
        long start = System.currentTimeMillis();
        logger.info("接口响应时间：{}", start);
        String originalFilename=null;
        if(file!=null){
            originalFilename = file.getOriginalFilename();
        }
        logger.info("提交数据页上传文件接口，borrowerId：{}，projectId：{},fileName:{},content:{}",applicationId,projectId, originalFilename,content);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            if(file==null&&content==null){
                baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
            }else {
                BusinessVo<String> businessVo=submitDataPageService.upload(token,applicationId,projectId,file,content);
                baseResultVo.setResult(businessVo);
                baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
                baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
            }
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("提交数据页上传文件或保存数据失败：fileName：{}，异常栈：{}，",originalFilename,e);
        }
        long end = System.currentTimeMillis();
        baseResultVo.setCurrentDate(end);
        logger.info("接口耗时：{} ms", end-start);
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "提交申请或者暂存接口", notes = "提交申请或者暂存接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "提交状态 1 提交  0 暂存", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "mark", value = "提交备注", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/submitOrScratch")
    public BaseResultVo<String> submitOrScratch(@RequestParam(value = "token") String token,
                                             @RequestParam(value = "applicationId") Integer applicationId,
                                             @RequestParam(value = "status") String status,
                                             @RequestParam(value = "mark",required = true) String mark){
        logger.info("提交申请或者暂存接口，borrowerId：{}，status：{},mark:{}",applicationId,status, mark);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<String> businessVo=submitDataPageService.submitOrScratch(token,applicationId,status,mark);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("提交申请或者暂存失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "查询所有待上传资料(查询模板项目)或暂存资料接口", notes = "查询所有待上传资料(查询模板项目)接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/selectSubmitDataPage")
    public BaseResultVo<SubmitDataPageVo> selectProjects(@RequestParam(value = "token") String token,
                                                                         @RequestParam(value = "applicationId") Integer applicationId){
        logger.info("查询所有待上传资料(查询模板项目)接口，applicationId：{}",applicationId);
        BaseResultVo<SubmitDataPageVo> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<SubmitDataPageVo> businessVo=submitDataPageService.selectProjects(applicationId);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询所有待上传资料(查询模板项目)或暂存资料失败，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "设定放款时间接口", notes = "设定放款时间接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "applicationId", value = "借贷申请id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "loanTime", value = "放款时间 yyyy-MM-dd", required = false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/setLoanTime")
    public BaseResultVo<String> setLoanTime(@RequestParam(value = "token") String token,
                                                                         @RequestParam(value = "applicationId") Integer applicationId,
                                                                         @RequestParam(value = "loanTime") String loanTime){
        logger.info("设定放款时间接口，applicationId：{},loanTime",applicationId,loanTime);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            if(StringUtils.isBlank(loanTime)){
                baseResultVo.setCode(ResultConstansUtil.PARAMS_NOT_NULL_CODE);
                baseResultVo.setMessage(ResultConstansUtil.PARAMS_NOT_NULL_DESC);
                return baseResultVo;
            }
            Date loanDate = DateUtils.stringToDate(loanTime, DateUtils.yyyyMMdd_format);
            BusinessVo<String> businessVo=submitDataPageService.setLoanTime(applicationId,loanDate);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("设定放款时间接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

    @ApiOperation(value = "删除上传数据接口", notes = "删除上传数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "submitProjectId", value = "上传数据id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/deleteSubmitProject")
    public BaseResultVo<String> deleteSubmitProject(@RequestParam(value = "token") String token,
                                                                         @RequestParam(value = "submitProjectId") Integer submitProjectId
                                                                         ){
        logger.info("删除上传数据接口，submitProjectId：{}",submitProjectId);
        BaseResultVo<String> baseResultVo = new BaseResultVo<>();
        try {
            BusinessVo<String> businessVo=submitDataPageService.deleteSubmitProject(submitProjectId);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("删除上传数据接口，异常栈：{}，",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


}
