package net.bestjoy.cloud.datasource.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Objects;

/***
 * 拦截SwitchDs注解，切换动态数据源
 * @author ray
 *
 */
@Aspect
@Order(-10)// 保证该AOP在@Transactional之前执行
@Slf4j
public class DynamicDataSourceAspect {

    @Before("@within(net.bestjoy.cloud.datasource.dynamic.DS) || @annotation(net.bestjoy.cloud.datasource.dynamic.DS)")
    public void changeDataSource(JoinPoint joinPoint) {
        // 获取方法上的注解
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DS annotation = method.getAnnotation(DS.class);

        if (Objects.isNull(annotation)) {
            //方法上没有注解，获取类上的注解
            annotation = joinPoint.getTarget().getClass().getAnnotation(DS.class);
        }

        if (Objects.isNull(annotation)) {
            //获取接口上的注解
            for (Class<?> cls : joinPoint.getTarget().getClass().getInterfaces()) {
                annotation = cls.getAnnotation(DS.class);
                if (annotation != null) {
                    break;
                }
            }
        }

        if (Objects.isNull(annotation)) {
            return;
        }

        //切换数据源
        DynamicDataSourceContext.setDataSourceLookupKey(annotation.value());

        if (log.isDebugEnabled()) {
            log.debug("change datasource:{}", annotation.value());
        }
    }

    @After("@within(net.bestjoy.cloud.datasource.dynamic.DS) || @annotation(net.bestjoy.cloud.datasource.dynamic.DS)")
    public void clearDataSource() {
        DynamicDataSourceContext.clearDataSourceLookupKey();
    }
}
