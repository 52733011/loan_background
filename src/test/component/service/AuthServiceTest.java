package component.service;

import com.alibaba.fastjson.JSON;
import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.entity.po.ContactInfo;
import com.xiaochong.loan.background.entity.vo.BusinessVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.AuthService;
import com.xiaochong.loan.background.utils.enums.ContactInfoRelationEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiaxing on 2017/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class AuthServiceTest {

    @Resource(name = "authService")
    private AuthService authService;

    @Test
    public void submitContactTest() throws DataDisposeException {
        List<ContactInfo> contactInfos = new ArrayList<>();
        String orderToken = "5b5251d6-01fb-4081-b7df-319d62aacdf1";
        ContactInfo contactInfo1 = new ContactInfo();
        contactInfo1.setRelation(ContactInfoRelationEnum.TYPE_FATHER.getType());
        contactInfo1.setRealName("父亲");
        contactInfo1.setPhone("13556466645");
        contactInfos.add(contactInfo1);
        ContactInfo contactInfo2 = new ContactInfo();
        contactInfo2.setRelation(ContactInfoRelationEnum.TYPE_MOTHER.getType());
        contactInfo2.setRealName("母亲");
        contactInfo2.setPhone("13556466645");
        contactInfos.add(contactInfo2);
        ContactInfo contactInfo3 = new ContactInfo();
        contactInfo3.setRelation(ContactInfoRelationEnum.NO_TYPE.getType());
        contactInfo3.setRelationName("其他");
        contactInfo3.setRealName("其他");
        contactInfo3.setPhone("13556466645");
        contactInfos.add(contactInfo3);
        BusinessVo<String> stringBusinessVo = authService.submitContact(orderToken, JSON.toJSONString(contactInfos),
                "是大法官好vdfjhbgejhdejh", "省", "市", "区", "常住");
        System.out.println(stringBusinessVo);
    }


    @Test
    public void getTongdunEducation() throws Exception {
        authService.getTongdunEducation("王昕","141124199312200087","b11082c8-ae10-4905-9458-61dad8fe281e");
    }
}