package net.bestjoy.cloud.core.resolver;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/***
 * 断言切面
 *
 * @author ray
 */
@Aspect
@Component
public class AssertResolverAspect {
    //todo  定义threadLocal 存储不通过的校验参数

    @Pointcut("@annotation(net.bestjoy.cloud.core.annotation.NeedValidate)")
    public void needValidatePointcut() {
    }

    @SneakyThrows
    @Around(value = "needValidatePointcut()")
    public Object validateHandler(ProceedingJoinPoint joinPoint) {

        Signature signature = joinPoint.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();

        Parameter[] parameters = method.getParameters();


        return joinPoint.proceed();
    }
}
