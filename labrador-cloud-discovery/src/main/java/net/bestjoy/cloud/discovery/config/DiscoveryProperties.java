package net.bestjoy.cloud.discovery.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 注册中心配置
 * @author ray
 */
@Data
@ToString
@ConfigurationProperties(prefix = "bestjoy.discovery")
public class DiscoveryProperties {
    /**
     * 服务名称
     */
    private String serviceName;

    /***
     * 发现服务地址
     */
    private String serverAddr;

//todo add...
}
