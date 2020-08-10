package net.bestjoy.cloud.security.core;

import lombok.Data;
import lombok.ToString;

/***
 * 访问令牌
 * @author ray
 */
@Data
@ToString
public class AccessToken {

    /***
     * 访问令牌
     */
    private String accessToken;

    /***
     * 刷新令牌，过期通过该token刷新
     */
    private String refreshToken;

    /***
     * 令牌生效时间
     */
    private long effectTime;

    /***
     * 过期时间
     */
    private long expireInterval;

    /**
     * 刷新令牌生效时间
     */
    private long reEffectTime;

    /***
     * 刷新令牌过期时间
     */
    private long reExpireInterval;
}
