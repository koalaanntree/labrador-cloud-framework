package net.bestjoy.cloud.logger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/***
 * 摘要日志注解，作用在类和方法上
 * @author ray
 */
@Documented
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface DigestLogger {
}
