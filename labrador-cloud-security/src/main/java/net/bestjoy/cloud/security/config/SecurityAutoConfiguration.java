package net.bestjoy.cloud.security.config;

import net.bestjoy.cloud.security.encrypt.jwt.JwtHelper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * security统一配置
 * @author ray
 */
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityAutoConfiguration {

    @Bean
    public JwtHelper jwtHelper() {
        return new JwtHelper();
    }
}
