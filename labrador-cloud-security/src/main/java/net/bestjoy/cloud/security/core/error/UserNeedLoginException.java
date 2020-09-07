package net.bestjoy.cloud.security.core.error;


import net.bestjoy.cloud.core.error.BusinessException;

/***
 * 用户未登录异常
 * @author ray
 */
public final class UserNeedLoginException extends BusinessException {
    /***
     * 用户未登录错误码
     */
    private static final String ERROR_CODE = "A00001";

    private static final String ERROR_MESSAGE = "用户未登录，或登录已过期";

    public UserNeedLoginException() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
