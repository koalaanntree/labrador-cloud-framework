package net.bestjoy.cloud.core.error;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 业务异常
 *
 * @author ray
 */
@Data
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    /**
     * 错误信息
     */
    private ErrorCodeAndMessage error;
    /**
     * 异常对象，用于返回信息
     */
    private Object data;

    public BusinessException(ErrorCodeAndMessage error) {
        this.error = error;
    }

    public BusinessException(Integer errorCode, String errorMessage, String data) {
        this.error = ErrorCodeAndMessage.create(errorCode, errorMessage);
        this.data = data;
    }

    public BusinessException(ErrorCodeAndMessage error, Throwable cause, String data) {
        super(JSONObject.toJSONString(error), cause);
        this.data = data;
    }
}
