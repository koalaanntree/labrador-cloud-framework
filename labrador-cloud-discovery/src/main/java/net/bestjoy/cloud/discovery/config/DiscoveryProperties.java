package net.bestjoy.cloud.discovery.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 注册中心配置
 * @author ray
 */
@Data
@ToString
@ConfigurationProperties("bestjoy.discovery")
public class DiscoveryProperties {

    @Value("${bestjoy.discovery.service:${spring.application.name:}}")
    private String serviceName;

    /***
     * 发现服务地址
     */
    private String discoveryServerAddr;

//todo add...
}
