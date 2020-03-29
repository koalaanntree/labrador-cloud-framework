package net.bestjoy.cloud.web.config;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.logger.config.WebLoggerAutoConfiguration;
import net.bestjoy.cloud.web.error.DefaultGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/***
 * web模块配置
 * @author ray
 */
@Slf4j
@Configuration
@Import(DefaultGlobalExceptionHandler.class)
@EnableConfigurationProperties({WebProperties.class})
@ImportAutoConfiguration(classes = {WebLoggerAutoConfiguration.class})
public class WebAutoConfiguration {

    private final WebProperties webProperties;

    public WebAutoConfiguration(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Bean
    @ConditionalOnExpression("${bestjoy.web.enable-cors:true}")
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置你要允许的网站域名，如果全允许则设为 *
        config.addAllowedHeader(StringUtils.isEmpty(webProperties.getCorsHeader()) ? "*" : webProperties.getCorsHeader());
        config.addAllowedOrigin(StringUtils.isEmpty(webProperties.getCorsOrigin()) ? "*" : webProperties.getCorsOrigin());
        config.addAllowedMethod(StringUtils.isEmpty(webProperties.getCorsMethod()) ? "*" : webProperties.getCorsMethod());
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        // 这个顺序很重要哦，为避免麻烦请设置在最前
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
