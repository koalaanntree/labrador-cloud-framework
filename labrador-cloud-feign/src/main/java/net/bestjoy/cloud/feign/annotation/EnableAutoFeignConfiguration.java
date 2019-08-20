package net.bestjoy.cloud.feign.annotation;


import net.bestjoy.cloud.feign.config.FeignConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/***
 * 注解开启自动加载feign配置项
 *
 * @author ray
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FeignConfiguration.class)
public @interface EnableAutoFeignConfiguration {
}
