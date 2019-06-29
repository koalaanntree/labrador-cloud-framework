package net.bestjoy.cloud.error.bean;

/**
 * 异常码
 *
 * @author ray
 */
public class ErrorCodeAndMessage extends CodeAndMessage {

    public static ErrorCodeAndMessage create(
            String errorCode, String errorMessage) {

        ErrorCodeAndMessage errorCodeAndMessage = new ErrorCodeAndMessage();
        errorCodeAndMessage.setCode(errorCode);
        errorCodeAndMessage.setMessage(errorMessage);

        return errorCodeAndMessage;
    }
}
