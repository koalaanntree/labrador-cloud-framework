package net.bestjoy.cloud.datasource.config;

import com.zaxxer.hikari.HikariDataSource;
import net.bestjoy.cloud.datasource.dynamic.DynamicDataSource;
import net.bestjoy.cloud.datasource.dynamic.DynamicDataSourceAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/***
 * 动态数据源配置
 * @author ray
 */
@EnableAspectJAutoProxy
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {

    @Bean("dynamicDataSourceAspect")
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    @Bean("dataSource")
    @ConditionalOnMissingBean
    public DynamicDataSource dataSource(DynamicDataSourceProperties dynamicDataSourceProperties) {
        Assert.notNull(dynamicDataSourceProperties.getDefaultDataSource(), "default datasource cannot be empty");

        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        Map<Object, Object> dsMap = new HashMap<>(dynamicDataSourceProperties.getDynamic().size());

        dynamicDataSourceProperties.getDynamic().forEach((dsName, properties) -> {
            DataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
            dsMap.put(dsName, dataSource);
        });

        //默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dsMap.get(dynamicDataSourceProperties.getDefaultDataSource()));

        dynamicDataSource.setTargetDataSources(dsMap);
        
        return dynamicDataSource;
    }

}
