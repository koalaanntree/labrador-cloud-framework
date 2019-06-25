package net.bestjoy.cloud.core.error;

/**
 * 异常码
 *
 * @author ray
 */
public class ErrorCodeAndMessage extends CodeAndMessage {

    public static ErrorCodeAndMessage create(
            Integer errorCode, String errorMessage) {
        ErrorCodeAndMessage errorCodeAndMessage = new ErrorCodeAndMessage();
        errorCodeAndMessage.setCode(errorCode);
        errorCodeAndMessage.setMessage(errorMessage);
        return errorCodeAndMessage;
    }
}
