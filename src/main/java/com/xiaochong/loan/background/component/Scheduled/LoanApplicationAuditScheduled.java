package com.xiaochong.loan.background.component.Scheduled;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.dao.LoanApplicationDao;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.service.LoanApplicationAuditService;
import com.xiaochong.loan.background.service.RepaymentRecordService;
import com.xiaochong.loan.background.utils.CollectionUtil;
import com.xiaochong.loan.background.utils.DateUtils;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wujiaxing on 2017/9/12 .
 * 计算逾期定时任务
 */
@Component
public class LoanApplicationAuditScheduled {

    private Logger logger = LoggerFactory.getLogger(LoanApplicationAuditScheduled.class);

    @Resource(name = "loanApplicationAuditService")
    private LoanApplicationAuditService loanApplicationAuditService;

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private RepaymentRecordService repaymentRecordService;

    @Scheduled(cron="0 0 0 * * ?")//每天执行一次
    public void orderFlowAuthScheduled() {
        LogTrace.beginTrace("开始执行逾期罚息计算定时任务");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        long begin=System.currentTimeMillis();
        logger.info("开始执行逾期罚息计算定时任务");
        LoanApplication searchLoanApplication = new LoanApplication();
        searchLoanApplication.setStatus(LoanApplicationStatusEnum.LOANED.getType());
        List<LoanApplication> loanApplications = loanApplicationDao.selectLoanApplication(searchLoanApplication);
        if(CollectionUtil.isBlank(loanApplications)){
            logger.warn("计算逾期定时任务获取放款申请失败!");
            return;
        }
        for (LoanApplication loanApplication:loanApplications) {
            try {
                loanApplicationAuditService.repaymentPlanOverdueByLoanApplication(loanApplication);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("逾期罚息计算定时任务完成，用时{}ms",System.currentTimeMillis()-begin);
        LogTrace.info("开始执行逾期罚息计算定时任务","orderFlowAuthScheduled", DateUtils.format(new Date(),DateUtils.yyyyMMdd_format));
        LogTrace.endTrace();
    }

    /**
     * 逾期催收计算
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void overDueAuthScheduled() {
        LogTrace.beginTrace("开始执行 逾期催收 计算定时任务...");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        long begin=System.currentTimeMillis();
        logger.info("开始执行 逾期催收 计算定时任务");
        try {
            //更新催收表
            //逾期期数  在逾期总本息  最长逾期天数
            repaymentRecordService.overDueStatisticsScheduled();
        }catch (Exception e){
            logger.error("逾期催收计算失败", e);
        }
        logger.info("逾期催收计算定时任务完成，用时{}ms",System.currentTimeMillis()-begin);
        LogTrace.info("开始执行 逾期催收 计算定时任务","overDueAuthScheduled", DateUtils.format(new Date(),DateUtils.yyyyMMdd_format));
        LogTrace.endTrace();
    }

    /**
     * 账户余额自动扣款
     */
    @Scheduled(cron="0 0 23 * * ?")
    public void autoDebitScheduled() {
        LogTrace.beginTrace("开始执行账户余额自动扣款定时任务");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        long begin=System.currentTimeMillis();
        logger.info("开始执行账户余额自动扣款定时任务");
        try {
//            自动扣款 逾期天数最长的一期
            List<Integer> repaymentIds =repaymentRecordService.autoDebitScheduled();
            logger.info("自动还款的还款计划id：{}",repaymentIds);
        }catch (Exception e){
            logger.error("账户余额自动扣款失败", e);
        }
        logger.info("账户余额自动扣款定时任务完成，用时{}ms",System.currentTimeMillis()-begin);
        LogTrace.info("开始执行账户余额自动扣款定时任务","autoDebitScheduled", DateUtils.format(new Date(),DateUtils.yyyyMMdd_format));
        LogTrace.endTrace();
    }

    /**
     * 自动扫描更新催收逾期状态
     */
    @Scheduled(cron="0 0 2 * * ?")
    public void updateUrgeOverDueStatusScheduled() {
        LogTrace.beginTrace("开始执行自动扫描更新 催收逾期状态 定时任务");
        logger.info("traceId:{}", LogTrace.getTrace().getTraceId());
        long begin=System.currentTimeMillis();
        logger.info("开始执行自动扫描更新 催收逾期状态 定时任务");
        try {
            repaymentRecordService.updateUrgeOverDueStatusScheduled();
        }catch (Exception e){
            logger.error("自动扫描更新催收逾期状态失败", e);
        }
        logger.info("自动扫描更新催收逾期状态定时任务完成，用时{}ms",System.currentTimeMillis()-begin);
        LogTrace.info("开始执行自动扫描更新 催收逾期状态 定时任务","updateUrgeOverDueStatusScheduled", DateUtils.format(new Date(),DateUtils.yyyyMMdd_format));
        LogTrace.endTrace();
    }
}
