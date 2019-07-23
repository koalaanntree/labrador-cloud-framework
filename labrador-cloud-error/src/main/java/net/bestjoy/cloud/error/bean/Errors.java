package net.bestjoy.cloud.error.bean;

/**
 * 定义异常常量
 *
 * @author ray
 */
public interface Errors {

    /***
     * 业务异常常量
     */
    interface Biz {
        ErrorCodeAndMessage ARGS_NOT_EMPTY_ERROR = ErrorCodeAndMessage.create("000000", "请求参数不能为空");
        ErrorCodeAndMessage ILLEGAL_ARGUMENT_ERROR = ErrorCodeAndMessage.create("000001", "请求参数非法");
        ErrorCodeAndMessage ACCESS_DENIED = ErrorCodeAndMessage.create("000002", "拒绝访问");
        ErrorCodeAndMessage EXCEL_RESOLVE_ERROR = ErrorCodeAndMessage.create("000003", "excel解析异常");
    }

    /***
     * 系统异常常量
     */
    interface Sys {
        ErrorCodeAndMessage SYS_ERROR = ErrorCodeAndMessage.create("999999", "系统异常");
        ErrorCodeAndMessage DB_ERROR = ErrorCodeAndMessage.create("999998", "数据库异常");
        ErrorCodeAndMessage CACHE_ERROR = ErrorCodeAndMessage.create("999997", "缓存异常");
        ErrorCodeAndMessage NOT_FOUND_ERROR = ErrorCodeAndMessage.create("999996", "访问资源不存在");
        ErrorCodeAndMessage REPEAT_REQUEST_ERROR = ErrorCodeAndMessage.create("999995", "重复请求");
        ErrorCodeAndMessage SERVER_NOT_RESPONSE_ERROR = ErrorCodeAndMessage.create("999994", "服务未响应");
        ErrorCodeAndMessage ES_ERROR = ErrorCodeAndMessage.create("999993", "ES异常");
        ErrorCodeAndMessage UNSUPPORTED_MEDIA_TYPE_ERROR = ErrorCodeAndMessage.create("999992", "不支持当前媒体类型");
        ErrorCodeAndMessage METHOD_NOT_ALLOWED_ERROR = ErrorCodeAndMessage.create("999991", "不支持当前方法");
        ErrorCodeAndMessage ENCRYPT_ERROR = ErrorCodeAndMessage.create("900000", "加密失败");
    }
}
