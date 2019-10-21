package net.bestjoy.cloud.core.annotation;

import java.lang.annotation.*;

/***
 * 标识是否需要校验
 *
 * @author ray
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NeedValidate {

}
