package component.service;

import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.entity.vo.MenuWebappVo;
import com.xiaochong.loan.background.exception.DataDisposeException;
import com.xiaochong.loan.background.service.ResourcesWebappService;
import com.xiaochong.loan.background.utils.enums.ResourcesWebappTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wujiaxing on 2017/9/1.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppMain.class)
@WebAppConfiguration
public class ResourcesWebappServiceTest {

    @Resource(name = "resourcesWebappService")
    private ResourcesWebappService resourcesWebappService;

    @Test
    public void userLogin() throws DataDisposeException {
        List<MenuWebappVo> menuWebappVos = resourcesWebappService.queryMenuIn("79", ResourcesWebappTypeEnum.MENU);
        System.out.println(menuWebappVos);
    }

    @Test
    public void resourcesInitialization(){
        resourcesWebappService.resourcesInitialization();

    }

}