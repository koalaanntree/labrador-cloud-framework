package net.bestjoy.cloud.logger.context;

import net.bestjoy.cloud.logger.constant.LoggerConstant;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/***
 * 日志上下文
 *
 * @author ray
 */
public final class LoggerContext {
    /***
     * 清除context内容
     */
    public static void clear() {
        MDC.clear();
    }

    /***
     * 设置traceId
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        MDC.putCloseable(LoggerConstant._TRACE_ID, traceId);
    }

    /**
     * 获取traceId
     */
    public static String getTraceId() {
        //默认spring sleuth已写入。参看brave包
        return MDC.get(LoggerConstant._TRACE_ID);
    }

    /***
     * 获取spanId
     * @return
     */
    public static String getSpanId() {
        return MDC.get(LoggerConstant._SPAN_ID);
    }

    /**
     * 设置返回结果
     *
     * @param returnResult
     */
    public static void setReturnResult(String returnResult) {
        MDC.putCloseable(LoggerConstant._RETURN_RESULT, returnResult);
    }

    public static String getReturnResult() {
        return MDC.get(LoggerConstant._RETURN_RESULT);
    }

    /***
     * 设置参数列表
     * @param args
     * @return
     */
    public static void setArgs(String args) {
        MDC.putCloseable(LoggerConstant._ARGS, args);
    }

    /**
     * 获取参数列表
     *
     * @return
     */
    public static String getArgs() {
        return MDC.get(LoggerConstant._ARGS);
    }

    /**
     * 设置业务说明
     */
    public static void setBizDescription(String bizDescription) {
        MDC.putCloseable(LoggerConstant._BIZ_DESCRIPTION, bizDescription);
    }

    /**
     * 获取业务说明
     *
     * @return
     */
    public static String getBizDescription() {
        return MDC.get(LoggerConstant._BIZ_DESCRIPTION);
    }

    /***
     * 设置返回结果码
     */
    public static void setResultCode(String resultCode) {
        MDC.putCloseable(LoggerConstant._RESULT_CODE, resultCode);
    }

    /***
     * 获取结果码
     * @return
     */
    public static String getResultCode() {
        return MDC.get(LoggerConstant._RESULT_CODE);
    }

    /**
     * 设置耗时
     *
     * @param timeCost
     */
    public static void setTimeCost(Long timeCost) {
        MDC.putCloseable(LoggerConstant._TIME_COST, String.valueOf(timeCost));
    }

    /**
     * 获取耗时
     *
     * @return
     */
    public static Long getTimeCost() {
        String costStr = MDC.get(LoggerConstant._TIME_COST);

        if (StringUtils.isEmpty(costStr)) {
            return null;
        }

        return Long.valueOf(costStr);
    }
}
