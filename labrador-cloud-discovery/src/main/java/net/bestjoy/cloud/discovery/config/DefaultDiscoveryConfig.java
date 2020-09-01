package net.bestjoy.cloud.discovery.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * nacos注册中心发现服务
 *
 * @author ray
 */
@EnableConfigurationProperties(
        {DiscoveryProperties.class})
@Configuration
@ConditionalOnDiscoveryEnabled
public class DefaultDiscoveryConfig {

    @Resource
    private DiscoveryProperties discoveryProperties;

    @PostConstruct
    public void init() {
//        Assert.notNull(discoveryProperties.getServerAddr(), "注册中心服务地址未设置");
        //todo
    }
}
