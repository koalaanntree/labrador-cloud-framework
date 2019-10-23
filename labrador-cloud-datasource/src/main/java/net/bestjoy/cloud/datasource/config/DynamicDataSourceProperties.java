package net.bestjoy.cloud.datasource.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/***
 * 动态数据源配置
 * @author ray
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DynamicDataSourceProperties {

    /***
     * 指定默认数据源名称
     */
    private String defaultDataSource;

    /***
     * 动态数据源配置
     */
    private Map<String, DataSourceProperties> dynamic;
}
