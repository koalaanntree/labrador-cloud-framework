package net.bestjoy.cloud.core.error;


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
    private CodeAndMessage error;


    public SysException(ErrorCodeAndMessage error, Throwable cause) {
        super(JSONObject.toJSONString(error), cause);
    }

    public SysException(Integer errorCode, String errorMessage) {
        this.error = ErrorCodeAndMessage.create(errorCode, errorMessage);
    }
}
