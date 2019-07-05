package sql;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.dao.LoanApplicationDao;
import com.xiaochong.loan.background.dao.UrgeOverdueDao;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.service.LoanApplicationAuditService;
import com.xiaochong.loan.background.service.RepaymentRecordService;
import com.xiaochong.loan.background.utils.CollectionUtil;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
import com.xiaochong.loan.background.utils.enums.UrgeOverDueStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jinxin on 2017/10/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class OverdueTest {

    @Autowired
    private RepaymentRecordService repaymentRecordService;

    @Autowired
    private UrgeOverdueDao overdueDao;
    @Test
    public void  test(){
//
        List<Integer> integers = repaymentRecordService.autoDebitScheduled();
        System.out.println(integers);
//        repaymentRecordService.overDueStatisticsScheduled();

//        repaymentRecordService.updateUrgeOverDueStatusScheduled();
    }
   @Test
    public void  test2(){
//
//        repaymentRecordService.autoDebitScheduled();
//        repaymentRecordService.overDueStatisticsScheduled();
       List<Integer> integers = overdueDao.selectApplicationIdsByStatus(UrgeOverDueStatusEnum.URGEED_TODAY.getType());
       List<Integer> integers1 = overdueDao.selectBorrowerIdsByStatus("1");

       System.out.println(integers);
       System.out.println(integers1);
   }

   @Autowired
   private LoanApplicationDao loanApplicationDao;

    @Autowired
    private LoanApplicationAuditService loanApplicationAuditService;

   @Test
    public void  test3(){
       long begin=System.currentTimeMillis();
       LoanApplication searchLoanApplication = new LoanApplication();
       searchLoanApplication.setStatus(LoanApplicationStatusEnum.LOANED.getType());
       List<LoanApplication> loanApplications = loanApplicationDao.selectLoanApplication(searchLoanApplication);
       if(CollectionUtil.isBlank(loanApplications)){
           return;
       }
       for (LoanApplication loanApplication:loanApplications) {
           try {
               loanApplicationAuditService.repaymentPlanOverdueByLoanApplication(loanApplication);
           }catch (Exception e){
           }
       }

   }


}
