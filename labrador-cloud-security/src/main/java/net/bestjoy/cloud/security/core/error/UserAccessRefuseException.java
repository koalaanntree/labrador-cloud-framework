package net.bestjoy.cloud.security.core.error;


import net.bestjoy.cloud.core.error.BusinessException;

/***
 * 访问被拒绝异常
 * @author ray
 */
public final class UserAccessRefuseException extends BusinessException {

    private final static String ERROR_CODE = "A00002";

    private final static String ERROR_MESSAGE = "访问被拒绝";

    public UserAccessRefuseException() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
