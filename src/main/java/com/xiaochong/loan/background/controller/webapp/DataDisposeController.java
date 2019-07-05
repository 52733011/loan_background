package com.xiaochong.loan.background.controller.webapp;

import com.xiaochong.loan.background.service.DataDisposeService;
import com.xiaochong.loan.background.service.ResourcesWebappService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dataDispose")
public class DataDisposeController {


    @Resource(name = "resourcesWebappService")
    private ResourcesWebappService resourcesWebappService;

    @Resource(name = "dataDisposeService")
    private DataDisposeService dataDisposeService;


    /**
     * 认证流程顺序更新
     * @return String
     */
    @GetMapping("/merchantFlowFlash")
    public String merchantFlowFlash(){
        dataDisposeService.merchantFlowFlash();
        return "success";
    }


    /**
     * 旧订单兼容
     * @return String
     */
    @GetMapping("/oldOrderDataDispose")
    public String oldOrderDataDispose(){
        dataDisposeService.oldOrderDataDispose();
        return "success";
    }


    /**
     * 初始化角色权限
     * @return String
     */
    @GetMapping("/resourcesInitialization")
    public String defaultTemplateinit(){
        resourcesWebappService.resourcesInitialization();
        return "success";
    }
}
