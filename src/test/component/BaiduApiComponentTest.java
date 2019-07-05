package component;

import com.alibaba.fastjson.JSONObject;
import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.component.BaiduApiComponent;
import com.xiaochong.loan.background.utils.enums.BaiduCoordTypeEnum;
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
public class BaiduApiComponentTest implements ApplicationContextAware {

    @Resource(name = "baiduApiComponent")
    private BaiduApiComponent baiduApiComponent;

    public void submitOrderTest(){
//        riskDataComponent.submitOrder("","","");
    }


    @Test
    public void placeSuggestion(){
    	JSONObject json = baiduApiComponent.placeSuggestion("上海师范大学","全国");
    	System.out.println(json);
    }

    @Test
    public void geocoder(){
    	JSONObject json = baiduApiComponent.geocoder("29.605345","106.303353", BaiduCoordTypeEnum.GPS);
    	System.out.println(json);
    }


    @Test
    public void geoconv(){
    	JSONObject json = baiduApiComponent.geoconv("29.605345","106.303353");
    	System.out.println(json);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.setApplicationContext(applicationContext);
    }
}