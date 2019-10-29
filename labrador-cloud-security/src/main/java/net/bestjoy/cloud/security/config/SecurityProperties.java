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

    /***
     * token过期interval，单位分钟
     * 默认1天
     */
    private Integer tokenExpireInterval = 24 * 60;

    /***
     * refresh token过期interval，单位分钟
     * 默认30天
     */
    private Integer refreshTokenExpireInterval = 30 * 24 * 60;

    /**
     * 是否开启jwt加密
     */
    private boolean jwtEnable = false;

    /***
     * 签发者
     */
    private String jwtIssuer = "net.bestjoy";

    /***
     * 应用名称
     */
    private String jwtAppName;

    /***
     * jwt加密串
     */
    private String jwtSignKey;

    /***
     * jwt加密类型
     *
     * @see sun.security.ssl.SignatureAndHashAlgorithm.SignatureAlgorithm
     */
    private String jwtSignType;
}
