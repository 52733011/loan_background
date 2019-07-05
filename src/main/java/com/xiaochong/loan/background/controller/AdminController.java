package com.xiaochong.loan.background.controller;

import com.xiaochong.loan.background.entity.po.Admin;
import com.xiaochong.loan.background.entity.vo.BaseResultVo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.service.AdminService;
import com.xiaochong.loan.background.utils.BusinessConstantsUtils;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.ResultConstansUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "借贷背调测试接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @ApiOperation(value = "测试接口")
    @GetMapping("/test")
    public BaseResultVo getAdmin(){
        BaseResultVo resultVo = new BaseResultVo();
        resultVo.setCode(ResultConstansUtil.SYSTEM_ERROR_CODE);
        resultVo.setMessage(ResultConstansUtil.SYSTEM_ERROR_DESC);
        resultVo.setCurrentDate(DateUtils.getCurrentDataLong());
        BusinessVo<Admin> businessVo = new BusinessVo<>();
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_CODE);
        businessVo.setCode(BusinessConstantsUtils.SUCCESS_DESC);
        resultVo.setResult(businessVo);
        return resultVo;
    }

}
