package component;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.IncrementComponent;
import com.xiaochong.loan.background.utils.enums.CountTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by ray.liu on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class IncrementComponentTest {

    @Resource(name = "incrementComponent")
    private IncrementComponent incrementComponent;

    @Test
    public void getCountNoTest(){
        String countNo = incrementComponent.getCountNo(CountTypeEnum.ORDER);
        System.out.println(countNo);
    }
}