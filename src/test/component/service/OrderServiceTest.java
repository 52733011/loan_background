package component.service;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.OrderService;
import com.xiaochong.loan.background.utils.IdCardUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by wujiaxing on 2017/11/2.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class OrderServiceTest {

    @Resource(name = "orderService")
    private OrderService orderService;

    @Test
    public void submitContactTest() throws DataDisposeException {
        String sexByIdCard = IdCardUtil.getSexByIdCard("5226271999702054431");
        System.out.println(sexByIdCard);
    }


}