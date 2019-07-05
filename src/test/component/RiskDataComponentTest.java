package component;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.RiskDataComponent;
import com.xiaochong.loan.background.utils.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class RiskDataComponentTest implements ApplicationContextAware {

    @Resource(name = "riskDataComponent")
    private RiskDataComponent riskDataComponent;

    public void submitOrderTest(){
//        riskDataComponent.submitOrder("","","");
    }


    @Test
    public void riskReportTest(){
        riskDataComponent.getCreditReport("51013119980918001X","1709047660039");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}