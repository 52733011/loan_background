package akka;

import com.xc.logclient.utils.LogTrace;
import com.xiaochong.loan.background.utils.akkaLog.AsyncService;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2017/11/1.
 */
public class AsyncRiskService implements AsyncService {
    @Override
    public Object invoke(Object data) {


        System.out.println("hahahahhahahah");
//        System.out.println(LogTrace.getTrace().getTraceId());
//        try {
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return data;
    }
}
