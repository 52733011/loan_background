package component.service;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.ProxyUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by wujiaxing on 2017/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class ProxyUserServiceTest {

    @Resource(name = "proxyUserService")
    private ProxyUserService proxyUserService;

    @Test
    public void userLogin() throws DataDisposeException {
        String phone = "15278945612";
        String password = "111111";
        String code = "xxy6";
        String sms = "frog";
        String time = "111111";
        proxyUserService.userLogin(phone, password, code, sms, time);
    }
}