package com.xiaochong.loan.background.controller.manager;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.IncrementComponent;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.component.SessionComponent;
import com.xiaochong.loan.background.entity.po.ManageAdmin;
import com.xiaochong.loan.background.entity.po.MerchantRecharge;
import com.xiaochong.loan.background.entity.vo.BasePageInfoVo;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.MerchantRechargeVo;
import com.xiaochong.loan.background.service.MerchantAccountService;
import com.xiaochong.loan.background.utils.*;
import com.xiaochong.loan.background.utils.enums.CountTypeEnum;
import com.xiaochong.loan.background.utils.enums.UserLoginTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jinxin on 2017/8/15.
 */
@Api(value = "后台商户账户接口")
@RestController
@RequestMapping("/back/merchantaccound")
public class MerchantAccoundController {


    private Logger logger = LoggerFactory.getLogger(MerchantAccoundController.class);

    @Autowired
    private MerchantAccountService merchantAccountService;

    @Resource(name = "component.OssComponent")
    private OssComponent ossComponent;

    @Autowired
    private IncrementComponent incrementComponent;

    @Autowired
    private SessionComponent sessionComponent;

    @ApiOperation(value = "充值接口",notes = "充值接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "merchId", value = "商户id",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "count", value = "充值次数",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "rechargeType", value = "充值原因",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "rechargeMark", value = "充值备注",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "rechargeVoucher", value = "充值凭证",required=true, dataType = "File", paramType = "form"),
    })
    @PostMapping("/recharge")
    public BaseResultVo<String> recharge(
            @RequestParam(value = "token") String token,
            @RequestParam("merchId") Integer merchId,
            @RequestParam("count") Integer count,
            @RequestParam("rechargeType") String rechargeType,
            @RequestParam(value = "rechargeMark",required = false) String rechargeMark,
            @RequestParam(value = "rechargeVoucher") MultipartFile rechargeVoucher
    ){
        BaseResultVo<String> baseResultVo= new BaseResultVo<>();
        logger.info("充值接口：token:{},merchId:{},count:{},rechargeType:{},rechargeMark:{},rechargeVoucher:{}",token
        ,merchId,count,rechargeType,rechargeMark,rechargeVoucher);
        try {
            StringBuilder key = new StringBuilder(UserLoginTypeEnum.MANAGE.getType()).append("-").append(token);
            ManageAdmin manager = sessionComponent.getAttributeManageAdmin(key.toString());
            Integer createBy = manager.getId();
            Date now = new Date();
            String orderNo=DateUtils.getCurrentDateyymd()+incrementComponent.getCountNo(CountTypeEnum.ORDER);
            String rechargeVoucherName;
            rechargeVoucherName = UUID.randomUUID().toString().replaceAll("-", "").substring(0,8)+rechargeVoucher.getOriginalFilename();
            String url = ossComponent.uploadFile(rechargeVoucherName, rechargeVoucher);
            MerchantRecharge merchantRecharge = new MerchantRecharge();
            merchantRecharge.setMerchId(merchId);
            merchantRecharge.setCount(count);
            merchantRecharge.setRechargeNo(orderNo);
            merchantRecharge.setRechargeType(rechargeType);
            merchantRecharge.setRechargeMark(rechargeMark);
            merchantRecharge.setVoucherName(rechargeVoucherName);
            merchantRecharge.setVoucherUrl(url);
            merchantRecharge.setRechargeBy(createBy);
            merchantRecharge.setRechargeTime(now);
            BusinessVo<String> businessVo= merchantAccountService.merchantRecharge(merchantRecharge);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.ACCOUNT_RECHARGE_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.ACCOUNT_RECHARGE_ERROR_DESC);
            logger.error("账户充值失败",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }


    @ApiOperation(value = "充值中心查询接口",notes = "充值中心查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "merchId", value = "商户id",required=false, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",required=false, dataType = "String", paramType = "form"),
    })
    @PostMapping("/rechargeSearch")
    public BaseResultVo<BasePageInfoVo<MerchantRechargeVo>> rechargeSearch(
            @RequestParam(value = "token") String token,
            @RequestParam(value ="merchId",required = false) Integer merchId,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "endTime",required = false) String endTime,
            @RequestParam(value = "startTime",required = false) String startTime
    ){
        logger.info("充值接口：token:{},merchId:{},pageNum:{},pageSize:{},endTime:{},startTime:{}",token
                ,merchId,pageNum,pageSize,endTime,startTime);
        BaseResultVo<BasePageInfoVo<MerchantRechargeVo>> baseResultVo= new BaseResultVo<>();
        try {
            MerchantRecharge merchantRecharge = new MerchantRecharge();
            merchantRecharge.setStartTime(DateUtils.stringToDate(startTime,DateUtils.yyyyMMdd_format));
            merchantRecharge.setEndTime(DateUtils.stringToDate(endTime,DateUtils.yyyyMMdd_format));
            merchantRecharge.setMerchId(merchId);
            BusinessVo<BasePageInfoVo<MerchantRechargeVo>> businessVo=merchantAccountService.selectMerchantRecharge(merchantRecharge,pageNum,pageSize);
            baseResultVo.setResult(businessVo);
            baseResultVo.setCode(ResultConstansUtil.SUCCESS_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SUCCESS_DESC);
        }catch (Exception e){
            baseResultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            baseResultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("充值中心查询失败",e);
        }
        baseResultVo.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(baseResultVo));
        return baseResultVo;
    }

}
