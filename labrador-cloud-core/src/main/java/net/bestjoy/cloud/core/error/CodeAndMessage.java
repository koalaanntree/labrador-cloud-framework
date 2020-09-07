package net.bestjoy.cloud.core.error;

import lombok.Data;
import lombok.ToString;

/***
 * 返回码和返回信息父类
 * @author ray
 */
@Data
@ToString
public class CodeAndMessage {
    /**
     * 返回码
     */
    private String code;
    /***
     * 返回信息
     */
    private String message;

    public CodeAndMessage() {

    }

    public CodeAndMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CodeAndMessage create(String code, String message) {
        return new CodeAndMessage(code, message);
    }


}
