package com.xiaochong.loan.background.component.Scheduled;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.service.OrderFlowService;
import com.xiaochong.loan.background.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by wujiaxing on 2017/8/16 .
 * 订单定时任务
 */
@Component
public class OrderScheduled {

    private Logger logger = LoggerFactory.getLogger(OrderScheduled.class);

    @Resource(name = "orderFlowService")
    private OrderFlowService orderFlowService;

    @Scheduled(cron="0 0/3 * * * ?")//每5分钟执行一次
    public void orderFlowAuthScheduled() {
        LogTrace.beginTrace("开始执行订单认证信息扫描定时任务");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        long begin=System.currentTimeMillis();
        logger.info("开始执行订单认证信息扫描定时任务");
        orderFlowService.orderFlowAuthScheduled();
        logger.info("订单认证信息扫描定时任务完成，用时{}ms",System.currentTimeMillis()-begin);
        LogTrace.info("开始执行订单认证信息扫描定时任务","orderFlowAuthScheduled", DateUtils.format(new Date(),DateUtils.yyyyMMdd_format));
        LogTrace.endTrace();
    }
}
