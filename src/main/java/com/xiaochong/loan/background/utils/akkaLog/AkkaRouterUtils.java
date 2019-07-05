package com.xiaochong.loan.background.utils.akkaLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.xc.logclient.bo.TraceLine;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.japi.Function;
import akka.pattern.Patterns;
import akka.routing.RoundRobinPool;
import akka.util.Timeout;

/**
 * Akka异步工具（采用路由器）
 * 
 * @author dyf
 *
 */
public class AkkaRouterUtils {

	private static ActorSystem system = ActorSystem.create("background");

	public static AtomicInteger countTest = new AtomicInteger(1);
	/**
	 * 异步服务列表
	 */
	private static Map<String, Process> map = new ConcurrentHashMap<String, Process>();

	public static final String TRACEID = "traceId";

	public static final String PARENTID = "parentId";

	/**
	 * 得到ActorSystem
	 */
	public static ActorSystem getActorSystem() {
		return system;
	}
	
	
	/**
	 * 注册服务
	 * 
	 * @param serviceName
	 *            唯一
	 *            工作Actor
	 */
	public static void registerAsyncService(final String serviceName,
			 AsyncService asyncService) {

		if (map.containsKey(serviceName)) {
			throw new RuntimeException("该异步" + serviceName + "已注册,请更换!");
		}

		map.put(serviceName, new Process() {

			private ActorRef actorService = system.actorOf(
					Props.create(ActorService.class, CommonActor.class, serviceName,asyncService),
					serviceName);

			@Override
			public void process(Object msg, OnSuccess success,
					OnFailure failure, int timeoutSecond) {

				TraceLine traceLine=com.xc.logclient.utils.LogTrace.getTrace();
				Map<String, Object> traceMap = new HashMap<String, Object>();
				traceMap.put("TRACE",traceLine);
				traceMap.put("DATA",msg);
				Timeout timeout = new Timeout(Duration.create(timeoutSecond,
						"seconds"));
				Future<Object> future = Patterns
						.ask(actorService, traceMap, timeout);
				future.onSuccess(success, system.dispatcher());
				future.onFailure(failure, system.dispatcher());

			}
		});
	}

	public static Process getProcess(String serviceName) {
		if (!map.containsKey(serviceName)) {
			throw new RuntimeException("没有注册该异步Service" + serviceName);
		}

		return map.get(serviceName);
	}


}

class ActorService extends UntypedActor {

	private ActorRef router;

	private Class workerClass;
	
	private String serviceName;

	private AsyncService asyncService;

	public ActorService(Class workerClass, String serviceName) {
		this.workerClass = workerClass;
		this.serviceName = serviceName;
	}

	public ActorService(Class workerClass, String serviceName,AsyncService asyncService) {
		this.workerClass = workerClass;
		this.serviceName = serviceName;
		this.asyncService=asyncService;
	}

	/**
	 * 启动会执行
	 */
	@Override
	public void preStart() throws Exception {
		// DefaultResizer resizer = new DefaultResizer(100, 2000);
		if(this.asyncService==null){
			router = getContext().actorOf(
					new RoundRobinPool(500).withSupervisorStrategy(strategy)
							.props(Props.create(workerClass))
							.withDispatcher("my-forkjoin-dispatcher"),
					serviceName + "Router");
		}else{
			router = getContext().actorOf(
					new RoundRobinPool(500).withSupervisorStrategy(strategy)
							.props(Props.create(workerClass,this.asyncService))
							.withDispatcher("my-forkjoin-dispatcher"),
					serviceName + "Router");
		}

	}

	@Override
	public void onReceive(Object msg) throws Exception {
		router.tell(msg, getSender());
	}

	// 定义监督策略
	private SupervisorStrategy strategy = new OneForOneStrategy(3,
			Duration.create("1 minute"), new Function<Throwable, Directive>() {

				@Override
				public Directive apply(Throwable t) {
					t.printStackTrace();
					System.out.println("错误：" + t + ",继续运行");
					return SupervisorStrategy.resume();
				}
			});

}
