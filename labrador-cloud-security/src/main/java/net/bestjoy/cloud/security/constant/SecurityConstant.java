package net.bestjoy.cloud.security.constant;

/***
 * 常量
 * @author ray
 */
public interface SecurityConstant {

    /***
     * 授权token
     */
    String AUTH_HEADER = "bt-auth-token";

    /**
     * 匿名用户角色
     */
    String ROLE_ANONYMOUS = "role_anonymous";

    /**
     * 管理员角色
     */
    String ROLE_ADMIN = "role_admin";

    /**
     * 默认用户
     */
    String PRINCIPAL_ANONYMOUS = "anonymousUser";

    /***
     * security urls
     */
    String[] SECURITY_IGNORED_URLS = {
            "/v1/security/auth/login",
            "/v1/security/auth/registerAndLogin",
            "/v1/sys/subject/role/list",
            "/v1/sys/subject/permission/list"
    };
}
