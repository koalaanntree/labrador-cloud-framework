package net.bestjoy.cloud.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/***
 * @author ray
 */
@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"net.bestjoy.cloud.security"})
public class AuthorizationAutoConfiguration {
    @Autowired
    private AuthorizationProperties authorizationProperties;

    @PostConstruct
    public void config() {
        Assert.notNull(authorizationProperties.getSystemName(), "system name not null");
        Assert.notNull(authorizationProperties.getSystemVersion(), "system version not null");
    }
}
