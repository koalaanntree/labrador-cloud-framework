package net.bestjoy.cloud.security.web.auth.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 登录请求对象
 * @author ray
 */
@Data
@ToString
public class UserLoginRequest implements Serializable {
    /***
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
