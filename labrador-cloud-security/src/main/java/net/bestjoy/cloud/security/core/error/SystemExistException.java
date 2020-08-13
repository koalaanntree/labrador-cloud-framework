package net.bestjoy.cloud.security.core.error;

/***
 * 系统已存在异常
 * @author ray
 */
public class SystemExistException extends RuntimeException {

    public SystemExistException(String message) {
        super("system:" + message + ",already exist exception.");
    }

    public SystemExistException(Throwable throwable) {
        super(throwable);
    }
}
