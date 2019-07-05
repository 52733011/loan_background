package com.xiaochong.loan.background.controller.manager;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.po.Checkflow;
import com.xiaochong.loan.background.entity.po.MerchantinfoFlow;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.entity.vo.MerchantinfoFlowVo;
import com.xiaochong.loan.background.service.CheckFlowService;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认证流程
 * @author jjinxin
 *
 */
@Api(value = "后台商户认证流程管理")
@RestController
@RequestMapping("/back/checkflow")
public class CheckflowController {

	private Logger logger = LoggerFactory.getLogger(CheckflowController.class);

	@Resource(name = "checkFlowService")
	private CheckFlowService checkFlowService;
	
	@ApiOperation(value = "查询所有认证信息",notes = "查询所有认证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
	@GetMapping("/getLoanCheckflows")
	public BaseResultVo getLoanCheckflows(){
	    logger.info("查询所有认证信息");
		BaseResultVo result = new BaseResultVo<>();
		try {
            BusinessVo<List<Checkflow>> businessVo = checkFlowService.selectLoanCheckflow();
            result.setResult(businessVo);
			result.setCode(ResultConstansUtil.SUCCESS_CODE);
			result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
			result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("查询所有认证信息失败！",e);
		}
        LogTrace.info("response", JSON.toJSONString(result));
        result.setCurrentDate(System.currentTimeMillis());
		return result;
	}
	
	
	@ApiOperation(value = "查询商户设置的认证流程",notes = "根据id查询商户设置的认证流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商户id",required=true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
    })
	@GetMapping("/getLoanMerchantinfoFlow")
	public BaseResultVo<MerchantinfoFlowVo> getLoanMerchantinfoFlow(@RequestParam("id") Integer id){
        logger.info("查询商户设置的认证流程,商户id：{}",id);
		BaseResultVo<MerchantinfoFlowVo> result = new BaseResultVo<>();
		try {
            BusinessVo<MerchantinfoFlowVo> businessVo=checkFlowService.getMerchantinfoFlow(id);
			result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询商户设置的认证流程失败！",e);
		}
        LogTrace.info("response", JSON.toJSONString(result));
		result.setCurrentDate(System.currentTimeMillis());
		return result;
	}

	@ApiOperation(value = "新增或修改商户认证流程",notes = "新增或修改商户认证流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchId", value = "商户id",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "flow", value = "流程,loan_checkflow表的flowNo,示例:1,3 那么表示要经过1,3两个流程,多个认证流程以逗号分隔",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form")
    })
	@PostMapping("/insertOrUpdateLoanMerchantinfoFlow")
	public BaseResultVo<String> insertOrUpdateLoanMerchantinfoFlow(@RequestParam("merchId") Integer merchId, @RequestParam("flow") String flow){
        logger.info("查询商户设置的认证流程,merchId：{},flow:{}",merchId,flow);
	    BaseResultVo<String> result = new BaseResultVo<>();
		try {
            BusinessVo<String> businessVo=checkFlowService.insertOrUpdateLoanMerchantinfoFlow(merchId,flow);
            result.setResult(businessVo);
            result.setCode(ResultConstansUtil.SUCCESS_CODE);
            result.setMessage(ResultConstansUtil.SUCCESS_DESC);
        } catch (Exception e) {
            result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
            result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
            logger.error("查询商户设置的认证流程失败！",e);
		}
        LogTrace.info("response", JSON.toJSONString(result));
        result.setCurrentDate(System.currentTimeMillis());
		return result;
	}


}
