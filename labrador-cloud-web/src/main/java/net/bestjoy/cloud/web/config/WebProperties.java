package net.bestjoy.cloud.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * web模块配置项
 * @author ray
 */
@Data
@ConfigurationProperties(prefix = "bestjoy.web")
public class WebProperties {
    /**
     * 是否允许跨域
     */
    private boolean enableCors = false;

    /***
     * 允许跨域源
     */
    private String corsOrigin;

    /**
     * 允许跨域方法
     */
    private String corsMethod;
    /**
     * 允许跨域header
     */
    private String corsHeader;
}
