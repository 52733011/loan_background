package sql;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.dao.MerchDataTemplateDao;
import com.xiaochong.loan.background.dao.MerchantDao;
import com.xiaochong.loan.background.dao.MerchantinfoFlowDao;
import com.xiaochong.loan.background.entity.po.MerchDataTemplate;
import com.xiaochong.loan.background.entity.po.Merchantinfo;
import com.xiaochong.loan.background.entity.po.MerchantinfoFlow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jinxin on 2017/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class CheckflowTest {

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private MerchDataTemplateDao merchDataTemplateDao;
    @Test
    public  void  test(){
        MerchDataTemplate merchDataTemplate = new MerchDataTemplate();
        merchDataTemplate.setMerchId(1);
        int id=merchDataTemplateDao.insertTemplate(merchDataTemplate);
        System.out.println(merchDataTemplate.getId());

    }

}
