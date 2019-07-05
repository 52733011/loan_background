package com.xiaochong.loan.background.component.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;


/**
 * Created by ray.liu on 2017/4/15.
 */
public class ActorService extends UntypedActor {


    private ActorRef actorRef;

    private ComponentActor componentActor;

    public ActorService(ComponentActor componentActor) {
        this.componentActor = componentActor;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        actorRef.tell(o,getSender());
    }

    /**
     * 启动前创建子actor以及定义actor策略
     * @throws Exception
     */
    @Override
    public void preStart() throws Exception {
        actorRef = getContext().actorOf(Props.create(BaseActor.class, componentActor));
    }

}
