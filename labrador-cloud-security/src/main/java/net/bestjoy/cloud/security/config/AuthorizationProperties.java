package net.bestjoy.cloud.security.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/***
 * @author ray
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "bt.auth")
public class AuthorizationProperties {

    /***
     * 系统名称
     */
    private String systemName;

    /***
     * 系统首页
     */
    private String systemHomepage;

    /***
     * 系统描述
     */
    private String systemDescription;

    /**
     * 系统版本
     */
    private String systemVersion;
}
