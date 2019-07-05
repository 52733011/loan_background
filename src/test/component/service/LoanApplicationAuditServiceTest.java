package component.service;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.Scheduled.LoanApplicationAuditScheduled;
import com.xiaochong.loan.background.dao.LoanApplicationDao;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.LoanApplicationAuditService;
import com.xiaochong.loan.background.utils.CollectionUtil;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wujiaxing on 2017/9/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class LoanApplicationAuditServiceTest {

    private Logger logger = LoggerFactory.getLogger(LoanApplicationAuditScheduled.class);

    @Resource(name = "loanApplicationAuditService")
    private LoanApplicationAuditService loanApplicationAuditService;

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Test
    public void repaymentPlanOverdueScheduledTest() throws DataDisposeException {
        LoanApplication searchLoanApplication = new LoanApplication();
        searchLoanApplication.setStatus(LoanApplicationStatusEnum.LOANED.getType());
        List<LoanApplication> loanApplications = loanApplicationDao.selectLoanApplication(searchLoanApplication);
        if(CollectionUtil.isBlank(loanApplications)){
            logger.warn("计算逾期定时任务获取放款申请失败!");
            return;
        }
        for (LoanApplication loanApplication:loanApplications) {
            /*if(!loanApplication.getId().equals(188)){
                continue;
            }*/
            try {
                loanApplicationAuditService.repaymentPlanOverdueByLoanApplication(loanApplication);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}