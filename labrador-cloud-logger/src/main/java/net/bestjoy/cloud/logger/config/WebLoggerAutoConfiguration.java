package net.bestjoy.cloud.logger.config;

import net.bestjoy.cloud.logger.aspect.DigestLoggerAspect;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * 日志自动配置
 *
 * @author ray
 */
@EnableAspectJAutoProxy
@ImportResource(locations = {"classpath:logback-*.xml"})
@EnableConfigurationProperties({LoggerProperties.class})
public class WebLoggerAutoConfiguration implements WebMvcConfigurer {

    public WebLoggerAutoConfiguration(LoggerProperties loggerProperties) {
    }

    @Bean
    public DigestLoggerAspect digestLoggerAspect() {
        return new DigestLoggerAspect();
    }

    //    @Bean
//    public TraceLoggerContextFilter traceLoggerContextFilter() {
//        return new TraceLoggerContextFilter();
//    }
}
