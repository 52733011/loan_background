package com.xiaochong.loan.background.utils.akkaLog;

import akka.actor.UntypedActor;

import com.xc.logclient.bo.TraceLine;
import com.xc.logclient.utils.LogTrace;

import java.util.Map;

public class CommonActor extends UntypedActor {

	private AsyncService asyncService;

	public CommonActor(AsyncService asyncService) {
		this.asyncService = asyncService;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		Map<String,Object> map=(Map<String, Object>)msg;
		TraceLine traceLine=(TraceLine) map.get("TRACE");
		LogTrace.reBuildTrace(traceLine);
		Object data=map.get("DATA");
		Object returnValue=asyncService.invoke(data);
		getSender().tell(returnValue,getSelf());
        LogTrace.reEndTrace();
	}

}
