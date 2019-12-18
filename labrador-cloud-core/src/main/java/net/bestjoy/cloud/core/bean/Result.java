package net.bestjoy.cloud.core.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.bestjoy.cloud.error.bean.*;

import java.io.Serializable;

/**
 * 业务返回值封装 - 用于接口层
 *
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
public class Result<T> implements Serializable {

    protected final static String SUCCESS = "100000";

    /**
     * 业务返回值编码
     * 统一code长度6位
     */
    private String code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 当前时间
     */
    private long timestamp;
    /**
     * 用于追踪链路
     */
    private String traceId;
    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 业务返回值
     */
    private T result;

    public Result(String code, String message, T result) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.result = result;
    }

    public Result(String code, String message) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.result = null;
    }

    public Result(CodeAndMessage codeAndMessage, T result) {
        this(codeAndMessage.getCode(), codeAndMessage.getMessage(), result);
    }

    public Result(CodeAndMessage codeAndMessage) {
        this(codeAndMessage, null);
    }

    /**
     * 默认返回成功
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result success(T result) {
        return new Result<>(SUCCESS, null, result);
    }

    /***
     * 默认返回成功
     * @return
     */
    public static Result<?> success() {
        return new Result(SUCCESS, null);
    }

    /***
     * 是否返回成功结果
     * @param result
     * @return
     */
    public static boolean isSuccess(Result<?> result) {
        return (result != null && SUCCESS.equals(result.code)) ? true : false;
    }

    /**
     * 检查是否发生异常
     *
     * @param result
     */
    public static void checkErrorResponse(Result<?> result) {
        if (result == null) {
            throw new SysException(Errors.Sys.SERVER_NOT_RESPONSE_ERROR);
        }

        if (!Result.isSuccess(result)) {
            throw new BusinessException(result.getCode(), result.getMessage(), null);
        }
    }

    /***
     * 解析result
     * @param result
     * @param <T>
     * @return
     */
    public static <T> T parseResult(Result<T> result) {
        checkErrorResponse(result);
        return result.getResult();
    }

    /***
     * 返回失败
     * @param errorCode  错误码
     * @param msg  错误信息
     * @return
     */
    public static Result fail(String errorCode, String msg) {
        return new Result(errorCode, msg);
    }

    /***
     * 返回失败
     * @param error
     * @return
     */
    public static Result fail(ErrorCodeAndMessage error) {
        return fail(error.getCode(), error.getMessage());
    }
}
