package net.bestjoy.cloud.security.core;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * 授权用户对象
 *
 * @author ray
 */
@Data
@ToString
public class UserSession implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /***
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 签名
     */
    private String signature;

    /**
     * 用户角色列表
     */
    private Set<String> roles;
}
