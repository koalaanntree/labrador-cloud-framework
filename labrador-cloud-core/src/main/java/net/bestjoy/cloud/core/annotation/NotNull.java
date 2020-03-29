package net.bestjoy.cloud.core.annotation;

import java.lang.annotation.*;

/***
 * 判断参数是否为空
 * 需要 @NeedValidate 注解开启
 *
 * 可被继承
 * @author ray
 */
@Inherited//声明注解具有可继承性
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Assert
public @interface NotNull {
    /***
     * 关键字
     * @return
     */
    String key() default "";

    /***
     * 错误提示
     * @return
     */
    String errorMessage() default "参数{}不能为空";
}
