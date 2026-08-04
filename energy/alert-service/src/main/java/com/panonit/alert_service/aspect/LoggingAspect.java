package com.panonit.alert_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.panonit.alert_service.service..*.*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void serviceLogBefore(JoinPoint joinPoint) {
        log.info("[service]:[{}] <- {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void serviceLogAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[service]:[{}] -> {}", joinPoint.getSignature().getName(), result);
    }
}
