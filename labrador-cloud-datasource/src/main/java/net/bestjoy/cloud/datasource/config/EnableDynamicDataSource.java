package net.bestjoy.cloud.datasource.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 是否开启动态数据源
 *
 * @author ray
 */
@Target({ElementType.TYPE})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Import(DynamicDataSourceConfig.class)
public @interface EnableDynamicDataSource {

}
