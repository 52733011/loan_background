package sql;

import com.github.pagehelper.PageHelper;
import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.dao.LoanApplicationDao;
import com.xiaochong.loan.background.dao.RepaymentPlanDao;
import com.xiaochong.loan.background.dao.RepaymentSerialRecordDao;
import com.xiaochong.loan.background.entity.po.LoanApplication;
import com.xiaochong.loan.background.entity.po.RepaymentPlan;
import com.xiaochong.loan.background.entity.po.RepaymentSerialRecord;
import com.xiaochong.loan.background.utils.enums.LoanApplicationStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jinxin on 2017/9/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class LoanApplicationTest {

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Test
    public void test(){

        LoanApplication loanApplication = new LoanApplication();
        List<String> statusList = new ArrayList<>();
        statusList.add(LoanApplicationStatusEnum.RESUBMIT.getType());
        statusList.add(LoanApplicationStatusEnum.WAITING_SUBMIT.getType());
        statusList.add(LoanApplicationStatusEnum.REFUSED.getType());
        loanApplication.setStatusList(statusList);
        loanApplication.setMerchId(1);
        loanApplication.setAuditType("1");
        PageHelper.startPage(1, 10, true);
        List<LoanApplication> loanApplicationList = loanApplicationDao.selectLoanAuditApplicationListByStutus(loanApplication);
        for (LoanApplication l: loanApplicationList) {
            System.out.println(l.getStatus());
        }


    }


    @Autowired
    private RepaymentPlanDao repaymentPlanDao;

    @Test
    public  void  test2(){
        RepaymentPlan repaymentPlan = new RepaymentPlan();
        repaymentPlan.setWholeMoney(new BigDecimal("122.43522"));
        repaymentPlanDao.insertRepaymentPlan(repaymentPlan);

    }

    @Autowired
    private RepaymentSerialRecordDao repaymentSerialRecordDao;

   @Test
    public  void  test23(){
       RepaymentSerialRecord repaymentSerialRecord = new RepaymentSerialRecord();

       List<Map<String, Object>> map = repaymentSerialRecordDao.selectCountByRepaymentSerialRecord(repaymentSerialRecord);
       System.out.println(map);

   }


}
