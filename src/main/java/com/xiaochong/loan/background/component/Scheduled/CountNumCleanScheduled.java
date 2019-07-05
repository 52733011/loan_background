package com.xiaochong.loan.background.component.Scheduled;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.component.IncrementComponent;
import com.xiaochong.loan.background.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by wujiaxing on 2017/5/26 0026.
 * 自增编号每日清理定时
 */
@Component
public class CountNumCleanScheduled {

    private Logger logger = LoggerFactory.getLogger(CountNumCleanScheduled.class);

    @Resource(name = "incrementComponent")
    private IncrementComponent incrementComponent;

    @Scheduled(cron="0 0 0 * * ?")//每日0点执行
    public void runScheduled() {
        LogTrace.beginTrace("开始执行订单认证信息扫描定时任务");
        long begin=System.currentTimeMillis();
        logger.info("开始执行重置countNum定时任务");
        incrementComponent.clean();
        long l = System.currentTimeMillis() - begin;
        logger.info("重置countNum定时任务完成，用时{}ms",l);
        LogTrace.info("开始执行订单认证信息扫描定时任务","authInfoTimeSched", DateUtils.format(new Date(),DateUtils.yyyyMMdd_format));
        LogTrace.endTrace();
    }
}
