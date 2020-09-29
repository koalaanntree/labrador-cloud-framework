package net.bestjoy.cloud.security.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/***
 * security配置项
 * @author ray
 */
@Data
@ToString
@ConfigurationProperties(prefix = "bestjoy.security")
public class SecurityProperties {

    /***
     * 系统名称
     */
    @Value("${bestjoy.security.system-name:${spring.application.name}}")
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
    @Value("${bestjoy.security.system-version:${spring.application.version}}")
    private String systemVersion;

    /**
     * 忽略认证url集合
     */
    private List<String> ignoredUrls;
}
