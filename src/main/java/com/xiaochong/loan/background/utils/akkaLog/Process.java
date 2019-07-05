package com.xiaochong.loan.background.utils.akkaLog;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;

public interface Process {

	/**
	 * 
	 * @param msg 消息
	 * @param success 成功回调函数
	 * @param failure 超时回调函数
	 * @param timeoutSecond 超时时间
	 */
	void process(Object msg, OnSuccess success, OnFailure failure, int timeoutSecond);

}
