package net.bestjoy.cloud.datasource.dynamic;

import java.lang.annotation.*;

/***
 * 切换数据源注解
 * @author ray
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DS {
    /**
     * 数据源名称
     *
     * @return
     */
    String value() default "";
}
