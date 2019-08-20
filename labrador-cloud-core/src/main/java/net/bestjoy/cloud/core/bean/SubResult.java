package net.bestjoy.cloud.core.bean;

import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.error.bean.*;

/***
 * 含子code的返回结果
 * @author ray
 */
@Data
@ToString(callSuper = true)
public final class SubResult<T> extends Result {

    private String subCode;

    private String subMessage;

    public SubResult(String code, String message, T result) {
        super(code, message, result);
    }

    public SubResult(String code, String message) {
        super(code, message);
    }

    public SubResult(CodeAndMessage codeAndMessage, T result) {
        super(codeAndMessage.getCode(), codeAndMessage.getMessage(), result);
    }

    public SubResult(CodeAndMessage codeAndMessage) {
        this(codeAndMessage, null);
    }


    /**
     * 设置子信息
     *
     * @param subCode
     * @param subMessage
     */
    public void sub(String subCode, String subMessage) {
        this.subCode = subCode;
        this.subMessage = subMessage;
    }

    /***
     * 默认返回成功
     * @return
     */
    public static SubResult success() {
        return new SubResult(SUCCESS, null);
    }

    /***
     * 是否返回成功结果
     * @param result
     * @return
     */
    public static boolean isSuccess(SubResult result) {
        return (result != null && SUCCESS.equals(result.getCode())) ? true : false;
    }

    /***
     * 返回失败
     * @param errorCode  错误码
     * @param msg  错误信息
     * @return
     */
    public static SubResult fail(String errorCode, String msg) {
        return new SubResult(errorCode, msg);
    }

    /***
     * 返回错误
     * @param errorCode
     * @param msg
     * @param subCode
     * @param subMessage
     * @return
     */
    public static SubResult fail(String errorCode, String msg, String subCode, String subMessage) {
        SubResult subResult = new SubResult(errorCode, msg);
        subResult.sub(subCode, subMessage);
        return subResult;
    }

    /***
     * 返回失败
     * @param error
     * @return
     */
    public static SubResult fail(ErrorCodeAndMessage error) {
        return fail(error.getCode(), error.getMessage());
    }

    /***
     * 返回失败
     * @param error
     * @param subCode
     * @param subMessage
     * @return
     */
    public static SubResult fail(ErrorCodeAndMessage error, String subCode, String subMessage) {
        SubResult subResult = fail(error.getCode(), error.getMessage());
        subResult.sub(subCode, subMessage);

        return subResult;
    }
}
