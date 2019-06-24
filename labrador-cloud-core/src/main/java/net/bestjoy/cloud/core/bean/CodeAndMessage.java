package net.bestjoy.cloud.core.bean;

import lombok.Getter;

@Getter
public enum CodeAndMessage implements BusinessCodeAndMessage {
  OK(1, "正常"),
  ERROR(9, "程序错误"),
  ILLEGAL_ARGUMENT(10, "参数错误"),
  METHOD_NOT_ALLOWED(11, "不支持的方法"),
  UNSUPPORTED_MEDIA_TYPE(12, "不支持的媒体类型"),
  EXTERNAL_SERVICE_ERROR(20, "调用外部服务错误"),
  TOKEN_INVALID(100, "Token失效"),
  PERMISSION_DENIED(403001, "权限不足"),
  PERMISSION_DENIED_ORG(403002, "组织级的权限不足"),
  PERMISSION_DENIED_USER(403003, "用户级权限不足"),
  NOT_NULL(400005, "对象不能为空"),
  NOT_TRUE(400006, "执行结果非真"),
  API_DEPRECATED(300000, "接口已废弃");
  private Integer code;
  private String message;

  CodeAndMessage(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
}
