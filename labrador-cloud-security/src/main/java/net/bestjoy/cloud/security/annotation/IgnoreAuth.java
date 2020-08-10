package net.bestjoy.cloud.security.annotation;

import java.lang.annotation.*;

/***
 * 过滤授权注解
 *
 * 此注解是在启动时，在security过滤url中添加。所以需要指定路径
 * @author ray
 */
@Target(value = ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface IgnoreAuth {
    /***
     * 请求路径，需要完整路径
     * @return
     */
    String pathInfo();
}
