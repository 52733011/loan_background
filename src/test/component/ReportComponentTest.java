package component;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.ReportComponent;
import com.xiaochong.loan.background.component.RiskDataComponent;
import com.xiaochong.loan.background.component.akka.AkkaComponent;
import com.xiaochong.loan.background.entity.vo.AkkaRiskOrderParamsVo;
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
public class ReportComponentTest implements ApplicationContextAware {


    @Resource(name = "akkaComponent")
    private AkkaComponent akkaComponent;

    @Test
    public void assemblyReport() throws InterruptedException {
        AkkaRiskOrderParamsVo akkaRiskOrderParamsVo = new AkkaRiskOrderParamsVo();
        akkaRiskOrderParamsVo.setIdCard("141124199312200087");
        akkaRiskOrderParamsVo.setOrderNum("1710300160033");
//        akkaComponent.process(new ReportComponent(),akkaRiskOrderParamsVo);

        Thread.sleep(999999999);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}