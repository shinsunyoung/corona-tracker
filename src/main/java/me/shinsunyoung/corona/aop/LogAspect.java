package me.shinsunyoung.corona.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    // 해당 서비스의 모든 메소드에 로그 남김
    @Around("execution(* me.shinsunyoung.corona.services.CoronaVirusDataService.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Start at : {} / {}", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
        Object result = pjp.proceed();
        log.info("Finished at : {} / {}", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
        return result;
    }






}
