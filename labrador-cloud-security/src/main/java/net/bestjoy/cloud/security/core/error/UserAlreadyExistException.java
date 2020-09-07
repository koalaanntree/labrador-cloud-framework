package net.bestjoy.cloud.security.core.error;


import net.bestjoy.cloud.core.error.BusinessException;

/***
 * 用户已存在异常
 *
 * @author ray
 */
public class UserAlreadyExistException extends BusinessException {
    private final static String ERROR_CODE = "A00003";

    private final static String ERROR_MESSAGE = "用户已存在";

    public UserAlreadyExistException() {
        super(ERROR_CODE, ERROR_MESSAGE, null);
    }
}
