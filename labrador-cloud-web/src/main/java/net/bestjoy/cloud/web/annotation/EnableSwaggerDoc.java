package net.bestjoy.cloud.web.annotation;

import net.bestjoy.cloud.web.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/***
 * 是否开启swagger文档注解
 * @author ray
 */
@Target({ElementType.TYPE})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Import(SwaggerAutoConfiguration.class)
public @interface EnableSwaggerDoc {

}
