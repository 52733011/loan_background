package com.xiaochong.loan.background.component;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.dao.CheckflowDao;
import com.xiaochong.loan.background.entity.po.Checkflow;
import com.xiaochong.loan.background.service.CheckFlowService;
import com.xiaochong.loan.background.utils.akkaLog.AkkaRouterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 各种初始化都放这儿 
 * PostConstruct默认运行
 * @author dyf
 */
@Component
public class AppInit {


	@Resource(name = "checkflowDao")
	private CheckflowDao checkflowDao;

	@Autowired
    private ReportComponentLog reportComponentLog;

	private static Map<String,Checkflow> LOANCHECKFLOWNO_MAP=new HashMap<>();

	@PostConstruct
	public void init() {
        LogTrace.beginTrace("App init...");
		//查询认证流程并cache
		List<Checkflow> flowList=checkflowDao.selectLoanCheckflow();
		for(Checkflow flow:flowList){
            LOANCHECKFLOWNO_MAP.put(flow.getFlowNo(),flow);
		}
        AkkaRouterUtils.registerAsyncService("reportComponent",reportComponentLog);
		LogTrace.endTrace();
	}

	public static void putCheckFlowByFlowNo(Checkflow flow){
        LOANCHECKFLOWNO_MAP.put(flow.getFlowNo(),flow);
	}

	public static Checkflow getCheckflowByFlowNo(String flowNo){
		return LOANCHECKFLOWNO_MAP.get(flowNo);
	}

	public static void removeCheckFlowByFlowNo(String flowNo){
        LOANCHECKFLOWNO_MAP.remove(flowNo);
	}
	
}
