package net.bestjoy.cloud.security.web.auth.response;

import lombok.*;

import java.io.Serializable;

/***
 * 登录返回结果
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginResponse implements Serializable {
    /***
     * 用户id
     */
    private String userId;
    /***
     * 账户名
     */
    @NonNull
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 个性签名
     */
    private String signature;
    /***
     * 登录令牌
     */
    @NonNull
    private String token;
}
