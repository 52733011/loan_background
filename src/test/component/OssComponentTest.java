package component;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.OssComponent;
import com.xiaochong.loan.background.dao.AttributionInfoDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray.liu on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class OssComponentTest {

    @Resource(name = "component.OssComponent")
    private OssComponent ossComponent;

    @Resource(name = "attributionInfoDao")
    private AttributionInfoDao attributionInfoDao;

    @Test
    public void putTest() throws Exception {
        File file = new File("D:\\Mobile.txt");
        String ossUrl = ossComponent.uploadFile("Mobile.txt", new FileInputStream(file));
        System.out.println(ossUrl);
    }
}