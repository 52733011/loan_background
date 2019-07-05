package com.xiaochong.loan.background.controller.webapp;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.controller.base.BaseController;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.RateTemplateVo;
import com.xiaochong.loan.background.service.RateTemplateService;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;


@Api(value = "费率相关接口")
@RestController
@RequestMapping("/rate")
public class RateTemplateController extends BaseController{

    @Resource(name = "rateTemplateService")
    private RateTemplateService rateTemplateService;

    private Logger logger = LoggerFactory.getLogger(RateTemplateController.class);

    @ApiOperation(value = "新增费率", notes = "费率必要信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rateName", value = "费率名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "depositMoney", value = "保证金", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "depositType", value = "保证金类型（0：百分比，1：实际金额）", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "interestRate", value = "利率", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "overdueMoneyType", value = "逾期记发（0：本金,,1:本息）", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "overdueRate", value = "逾期费率", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "overdueConstant", value = "逾期常数", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "overdueType", value = "逾期记发（0:当期待还,1:全部待还）", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "serviceCharge", value = "一次性服务费", required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "stagingType", value = "分期方式 0,按月放款 1,按日放款", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/insertRate")
    public BaseResultVo<Boolean> insertRate(@RequestParam(value = "rateName")String rateName,
                                            @RequestParam(value = "depositMoney")BigDecimal depositMoney,
                                            @RequestParam(value = "depositType")Integer depositType,
                                            @RequestParam(value = "interestRate")BigDecimal interestRate,
                                            @RequestParam(value = "overdueMoneyType")Integer overdueMoneyType,
                                            @RequestParam(value = "overdueRate")BigDecimal overdueRate,
                                            @RequestParam(value = "overdueConstant")BigDecimal overdueConstant,
                                            @RequestParam(value = "overdueType")Integer overdueType,
                                            @RequestParam(value = "serviceCharge")BigDecimal serviceCharge,
                                            @RequestParam(value = "stagingType")Integer stagingType,
                                            @RequestParam(value = "token")String token) {
        BaseResultVo<Boolean> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;

        resultVo = this.paramsNotNullReturn(resultVo,token,rateName,depositMoney,depositType,
                interestRate,overdueMoneyType,overdueRate,overdueConstant,overdueType,
                serviceCharge,stagingType);
        if(ResultConstansUtil.PARAMS_NOT_NULL_CODE.equals(resultVo.getCode())){
            return resultVo;
        }
        BusinessVo<Boolean> businessVo = rateTemplateService.insertTemplate(token,rateName,depositMoney,depositType,
                interestRate,overdueMoneyType,overdueRate,overdueConstant,overdueType,
                serviceCharge,stagingType);
        resultVo.setResult(businessVo);
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }


    @ApiOperation(value = "费率信息查询", notes = "费率必要信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/list")
    public BaseResultVo<BasePageInfoVo<RateTemplateVo>> selectList(@RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                                   @RequestParam(value = "pageSize",required = false)Integer pageSize,
                                                                   @RequestParam(value = "token",required = false)String token){
        BaseResultVo<BasePageInfoVo<RateTemplateVo>> resultVo = new BaseResultVo<>();
        String code = ResultConstansUtil.SUCCESS_CODE;
        String message = ResultConstansUtil.SUCCESS_DESC;
        if (null != pageNum && null != pageSize && StringUtils.isNotBlank(token)){
            BusinessVo<BasePageInfoVo<RateTemplateVo>> businessVo = rateTemplateService.RateTemplateList(pageNum, pageSize,token);
            resultVo.setResult(businessVo);
        }else {
            code = ResultConstansUtil.PARAMS_NOT_NULL_CODE;
            message = ResultConstansUtil.PARAMS_NOT_NULL_DESC;
        }
        resultVo.setCode(code);
        resultVo.setMessage(message);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        LogTrace.info("response", JSON.toJSONString(resultVo));
        return resultVo;
    }
}
