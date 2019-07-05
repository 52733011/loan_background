package com.xiaochong.loan.background.controller.manager;

import com.alibaba.fastjson.JSON;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.entity.vo.*;
import com.xiaochong.loan.background.service.ManageOrderService;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台订单列表查询
 * @author jjinxin
 *
 */
@Api(value = "后台订单列表查询")
@RestController
@RequestMapping("/back/order")
public class OrderManageController {

	private Logger logger = LoggerFactory.getLogger(OrderManageController.class);

	@Autowired
	private ManageOrderService manageOrderService;
	
	@ApiOperation(value = "后台订单列表查询",notes = "后台订单列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token",required=true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "orderNo", value = "token",required=false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "pageNum", value = "当前页",required=true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据大小",required=true, dataType = "Integer", paramType = "form"),
    })
	@PostMapping("/orderListSearch")
	public BaseResultVo<BasePageInfoVo<OrderManageVo>> orderListSearch(
            @RequestParam(value ="orderNo",required = false) String orderNo,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ){
	    logger.info("后台订单列表查询");
		BaseResultVo<BasePageInfoVo<OrderManageVo>> result = new BaseResultVo<>();
		try {
            BusinessVo<BasePageInfoVo<OrderManageVo>> businessVo = manageOrderService.orderListSearch(orderNo,pageNum,pageSize);
            result.setResult(businessVo);
			result.setCode(ResultConstansUtil.SUCCESS_CODE);
			result.setMessage(ResultConstansUtil.SUCCESS_DESC);
		} catch (Exception e) {
			result.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
			result.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
			logger.error("后台订单列表查询失败！",e);
		}
        result.setCurrentDate(System.currentTimeMillis());
        LogTrace.info("response", JSON.toJSONString(result));
		return result;
	}
	
}
