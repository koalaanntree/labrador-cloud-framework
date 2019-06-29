package net.bestjoy.cloud.security.config;

import net.bestjoy.cloud.security.encrypt.jwt.JwtHelper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/***
 * security统一配置
 * @author ray
 */
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityAutoConfiguration {

    @Bean
    public JwtHelper jwtHelper(SecurityProperties securityProperties) {
        Assert.notNull(securityProperties.getJwtAppName(), "jwt app name not provider");

        return new JwtHelper(securityProperties);
    }
}
