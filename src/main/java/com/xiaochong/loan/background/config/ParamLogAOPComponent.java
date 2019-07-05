package com.xiaochong.loan.background.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by yehao on 17/6/19.
 */
@Aspect
@Component
public class ParamLogAOPComponent {

    public static final Logger logger = LoggerFactory.getLogger(ParamLogAOPComponent.class);

    @Before("execution(* com.xiaochong.loan.background.controller.*.*.*(..)) ")
    public void printParam(JoinPoint joinPoint) {
        logger.info("Method = {},Prams = {},ThreadName = {},ThreadID = {}", joinPoint.getSignature().toString(), joinPoint.getArgs(), Thread.currentThread().getName(), Thread.currentThread().getId());
    }
}