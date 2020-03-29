package net.bestjoy.cloud.core.advices;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.annotation.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/***
 * @author ray
 */
@Slf4j
@Component
@Aspect
public class AssertAdvices {

    @Around("@annotation(net.bestjoy.cloud.core.annotation.NeedValidate)")
    @SneakyThrows
    public Object assertAround(ProceedingJoinPoint joinPoint) {
        log.info("assert ...");



        return joinPoint.proceed();
    }
}
