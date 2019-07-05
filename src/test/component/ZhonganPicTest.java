package component;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.RiskDataComponent;
import com.xiaochong.loan.background.entity.vo.RiskResultVo;
import com.xiaochong.loan.background.exception.OKhttpException;
import com.xiaochong.loan.background.utils.OkHttpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinxin on 2017/11/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class ZhonganPicTest {



    String testUrl = "http://116.62.225.79:8081/api/zhongan/identity/identityImage";

    String oriUrl = "http://kpapi.xiaochong.com/api/zhongan/identity/identityImage";

    String prefix="data:image/png;base64,";

    String appAccount="jiedaibeidiao_test";
    String xiaochongbeidiao_test="xiaochongbeidiao_test";
    String riskSecretKey="123456";
    @Test
    public void test() throws OKhttpException {
        Map<String,String> map = new HashMap<>();
        String name="马鑫";
        String idCard="320821199209064912";
        map.put("name",name);
        map.put("idCard",idCard);
        map.put("app_account", appAccount);
        map.put("risk_secret_key", riskSecretKey);
        String post = OkHttpUtils.post(testUrl, map);

        System.out.println(post);
    }

    @Autowired
    private RiskDataComponent riskDataComponent;

    @Test
    public void riskComTest(){
        LogTrace.beginTrace("1111");
        String name="金鑫";
        String idCard="320821199209064912";

        RiskResultVo<String> stringRiskResultVo = riskDataComponent.zhongAnPicture(name, idCard);

        System.out.println(stringRiskResultVo.getData());


        LogTrace.endTrace();
    }

}
