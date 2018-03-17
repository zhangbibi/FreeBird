package com.freebird.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyaping on 17/8/12.
 */

@Aspect
@Component
public class MethodExpendTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(MethodExpendTimeAspect.class);

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.freebird.service..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
    }

    @After("webLog()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {
        logger.info(joinPoint.getSignature().getName() + "expend time : " + (System.currentTimeMillis() - startTime.get()) + "ms");
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("RESPONSE : " + ret);
    }
}
