package net.bestjoy.cloud.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * security配置项
 * @author ray
 */
@Data
@ConfigurationProperties(prefix = "bestjoy.security")
public class SecurityProperties {
}
