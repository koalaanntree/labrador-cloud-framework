package net.bestjoy.cloud.error.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 系统异常
 *
 * @author ray
 */
@Data
@AllArgsConstructor
public class SysException extends RuntimeException {
    /**
     * 异常对象
     */
    private ErrorCodeAndMessage error;


    public SysException(ErrorCodeAndMessage error, Throwable cause) {
        super(JSONObject.toJSONString(error), cause);
    }

    public SysException(String errorCode, String errorMessage) {
        this.error = ErrorCodeAndMessage.create(errorCode, errorMessage);
    }

    public SysException(ErrorCodeAndMessage error, String errorMessage) {
        this.error = ErrorCodeAndMessage.create(error.getCode(), errorMessage);
    }
}
