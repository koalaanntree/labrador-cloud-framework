package net.bestjoy.cloud.core.bean;

import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.error.bean.CodeAndMessage;
import net.bestjoy.cloud.error.bean.ErrorCodeAndMessage;

import java.io.Serializable;

/**
 * 业务返回值封装 - 用于接口层
 */
@Data
@ToString
public final class Result<T> implements Serializable {

    private final static Integer SUCCESS = 100000;

    /**
     * 业务返回值编码
     * 统一code长度6位
     */
    private Integer code;
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

    public Result(Integer code, String message, T result) {
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.result = result;
    }

    public Result(Integer code, String message) {
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
        return new Result(SUCCESS, null, result);
    }

    /***
     * 默认返回成功
     * @return
     */
    public static Result success() {
        return new Result(SUCCESS, null);
    }

    /***
     * 返回失败
     * @param errorCode  错误码
     * @param msg  错误信息
     * @return
     */
    public static Result fail(Integer errorCode, String msg) {
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
