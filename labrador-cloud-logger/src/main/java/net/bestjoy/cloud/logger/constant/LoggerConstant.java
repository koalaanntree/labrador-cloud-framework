package net.bestjoy.cloud.logger.constant;

/***
 * 日志常量
 *
 * @author ray
 */
public interface LoggerConstant {
    /**
     * traceId key
     * spring cloud sleuth 中写入的
     */
    String _TRACE_ID = "X-B3-TraceId";

    /***
     * 当前服务的id
     */
    String _SPAN_ID = "X-B3-SpanId";

    /***
     * 父span Id
     */
    String _PARENT_SPAN_ID = "X-B3-ParentSpanId";

    /***
     * 返回结果
     */
    String _RETURN_RESULT = "BJ-ReturnResult";

    /***
     * 参数列表
     */
    String _ARGS = "BJ-Args";

    /***
     * 业务说明
     */
    String _BIZ_DESCRIPTION = "BJ-BizDescription";

    /***
     * 返回结果码
     */
    String _RESULT_CODE = "BJ-ResultCode";

    /**
     * 耗时
     */
    String _TIME_COST = "BJ-TimeCost";
}
