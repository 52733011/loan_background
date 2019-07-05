package com.xiaochong.loan.background.component.akka;

import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ray.liu on 2017/4/15.
 */
public class BaseActor extends UntypedActor {

    private Logger logger = LoggerFactory.getLogger(BaseActor.class);

    private ComponentActor componentActor;

    public BaseActor(ComponentActor componentActor) {
        this.componentActor = componentActor;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try {
            componentActor.process(message);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
