package net.bestjoy.cloud.error.bean;

import lombok.Getter;
import net.bestjoy.cloud.core.bean.BusinessCodeAndMessage;
import net.bestjoy.cloud.core.bean.CodeAndMessage;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private Integer code;

    public String message;

    public BusinessException(Integer code) {
        this.code = code;
    }

    private BusinessException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public BusinessException() {
        this(CodeAndMessage.ERROR);
    }

    public BusinessException(String message) {
        this(CodeAndMessage.ERROR.getCode(), message);
    }

    public BusinessException(BusinessCodeAndMessage businessCodeAndMessage) {
        this(businessCodeAndMessage.getCode(), businessCodeAndMessage.getMessage());
    }

    public BusinessException(BusinessCodeAndMessage businessCodeAndMessage, String message) {
        this(businessCodeAndMessage.getCode(), message);
    }
}
