package net.bestjoy.cloud.security.web.auth.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/***
 * 用户信息返回对象
 * @author ray
 */
@Data
@ToString
public class UserInfoVO implements Serializable {
    /***
     * 用户id
     */
    private String userId;
    /***
     * 账户名
     */
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

    /**
     * 用户角色列表
     */
    private Set<String> roles;
}
