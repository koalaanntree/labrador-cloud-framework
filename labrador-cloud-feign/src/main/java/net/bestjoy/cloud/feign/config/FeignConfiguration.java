package net.bestjoy.cloud.feign.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;


/***
 * feign configuration
 *
 * @author ray
 */
public class FeignConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer feignProperties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("application-feign-config.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yamlPropertiesFactoryBean.getObject());

        return propertySourcesPlaceholderConfigurer;
    }

}
