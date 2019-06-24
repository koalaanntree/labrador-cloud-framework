package net.bestjoy.cloud.core.bean;

import java.io.Serializable;

/** 业务返回值封装 - 用于接口层 */
public final class Result<T> implements Serializable {
  /** 业务返回值编码 */
  private Integer code;
  /** 提示信息 */
  private String message;
  /** 当前时间 */
  private long timestamp;

  /** 业务返回值 */
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

  public Result(BusinessCodeAndMessage businessCodeAndMessage, T result) {
    this(businessCodeAndMessage.getCode(), businessCodeAndMessage.getMessage(), result);
  }

  public Result(T result) {
    this(CodeAndMessage.OK, result);
  }

  public Result(BusinessCodeAndMessage businessCodeAndMessage) {
    this(businessCodeAndMessage, null);
  }

  public Result() {
    this(CodeAndMessage.OK, null);
  }

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "Result{"
        + "code="
        + code
        + ", message='"
        + message
        + '\''
        + ", timestamp="
        + timestamp
        + ", result="
        + result
        + '}';
  }
}
