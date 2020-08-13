package net.bestjoy.cloud.security.core.error;


import net.bestjoy.cloud.core.error.BusinessException;

/***
 * token invalidate exception
 * @author ray
 */
public class AccessTokenInvalidateException extends BusinessException {
    private final static String ERROR_CODE = "A00004";

    private final static String ERROR_MESSAGE = "访问被拒绝";

    public AccessTokenInvalidateException() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
