package com.xiaochong.loan.background.component.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.stereotype.Component;

/**
 * Created by ray.liu on 2017/4/15.
 */
@Component("akkaComponent")
public class AkkaComponent {

    //创建actor系统
    private static final ActorSystem ACTOR_SYSTEM = ActorSystem.create("background");

    public void process(ComponentActor componentActor, Object message){
        ActorRef actorRef = ACTOR_SYSTEM.actorOf(Props.create(ActorService.class, componentActor));
        actorRef.tell(message,ActorRef.noSender());
    }
}
