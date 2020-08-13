package net.bestjoy.cloud.security.listener;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.annotation.IgnoreAuth;
import net.bestjoy.cloud.security.context.SecurityContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

/***
 * 过滤security注解
 * 将所有添加ignoreAuth注解的请求path添加到内存中
 * @author ray
 */
@Component
@Slf4j
public class IgnoreAuthListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(RestController.class);
        if (!CollectionUtils.isEmpty(beans)) {
            beans.forEach((key, value) -> {
                Method[] methods = ReflectionUtils.getAllDeclaredMethods(value.getClass());
                for (Method method : methods) {
                    IgnoreAuth ignoreAuth = method.getAnnotation(IgnoreAuth.class);
                    if (ignoreAuth == null) {
                        continue;
                    }

                    if (!SecurityContext.isContainSecurityExcludePath(ignoreAuth.pathInfo())) {
                        SecurityContext.addSecurityExcludePath(ignoreAuth.pathInfo());
                    }
                }
            });
        }
    }
}
