package net.bestjoy.cloud.error.annotation;

import java.lang.annotation.*;

/***
 * 判断参数是否为空
 *
 * @author ray
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {

    String errorMessage() default "参数不能为空";
}
