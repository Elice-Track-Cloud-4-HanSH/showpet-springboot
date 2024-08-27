package com.elice.showpet.article.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ArticleAop extends AopUtils {
    @Pointcut("execution( * com.elice.showpet.article.*.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void logBeforeExecute(JoinPoint joinPoint) {
        String args = argsToString(joinPoint.getArgs());
        log.info(
                "method: {}::{}{}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                args.isEmpty() ? "" : ", args: " + args
        );
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("method: {}::{}, error: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}