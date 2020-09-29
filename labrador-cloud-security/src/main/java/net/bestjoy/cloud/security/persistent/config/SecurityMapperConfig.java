package net.bestjoy.cloud.security.persistent.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ray
 */
@Configuration
@MapperScan("net.bestjoy.cloud.security.persistent.repository")
public class SecurityMapperConfig {
}
