package component.service;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.entity.po.AttributionInfo;
import com.xiaochong.loan.background.service.AttributionInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by wujiaxing on 2017/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class AttributionInfoServiceTest {

    @Resource(name = "attributionInfoService")
    private AttributionInfoService attributionInfoService;

    @Test
    public void insertByOSS() throws IOException {
        String url = "http://xcbd-test.oss-cn-hangzhou.aliyuncs.com/Mobile.txt?Expires=1818404021&OSSAccessKeyId=LTAIlQ1L6DaY5Sdo&Signature=RjYQefBGOn302kdWC9CRsn8WVyI%3D";
        attributionInfoService.insertByOSS(url);
    }

    @Test
    public void attributionQuery() throws IOException {
        String num = "02152723014";
        AttributionInfo attributionInfo = attributionInfoService.attributionQuery(num);
        System.out.println(attributionInfo);
    }
}