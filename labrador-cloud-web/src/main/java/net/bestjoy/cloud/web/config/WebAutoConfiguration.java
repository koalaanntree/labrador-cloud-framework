package net.bestjoy.cloud.web.config;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.logger.config.WebLoggerAutoConfiguration;
import net.bestjoy.cloud.web.error.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/***
 * web模块配置
 * @author ray
 */
@Slf4j
@Configuration
@Import(GlobalExceptionHandler.class)
@EnableConfigurationProperties({WebProperties.class})
@ImportAutoConfiguration(classes = {WebLoggerAutoConfiguration.class})
public class WebAutoConfiguration {

    private final WebProperties webProperties;

    public WebAutoConfiguration(WebProperties webProperties) {
        this.webProperties = webProperties;
    }


    @Bean
    @ConditionalOnExpression("${bestjoy.web.enable-cors:true}")
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        log.debug("注册 CORS filter");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
