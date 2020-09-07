package net.bestjoy.cloud.logger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 日志配置
 * @author ray
 */
@Data
@ConfigurationProperties(prefix = "bestjoy.logger")
public final class LoggerProperties {

}
