package akka;

import akka.dispatch.OnFailure;
import com.xiaochong.loan.background.AppMain;
import com.xiaochong.loan.background.utils.akkaLog.Process;
import akka.dispatch.OnSuccess;
import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.utils.akkaLog.AkkaRouterUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by admin on 2017/11/1.
 */

public class Test {
    public static void main(String[] args) {
        LogTrace.beginTrace("begin");
        System.out.println(LogTrace.getTrace().getTraceId());
        AkkaRouterUtils.registerAsyncService("test",new AsyncRiskService());
        Thread thread = Thread.currentThread();
        System.out.println(thread);
        Process pro= AkkaRouterUtils.getProcess("test");
        pro.process("ABC", new OnSuccess() {
            @Override
            public void onSuccess(Object o) throws Throwable {
                  System.out.println("o:"+o);
                Thread thread = Thread.currentThread();

                System.out.println(thread);
            }
        }, null
        ,3);
        LogTrace.endTrace();
    }
}
