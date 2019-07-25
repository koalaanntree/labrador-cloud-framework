package net.bestjoy.cloud.logger.util;

import net.bestjoy.cloud.logger.Loggers;
import net.bestjoy.cloud.logger.context.LoggerContext;

/***
 * 日志工具
 * @author ray
 */
public final class LoggerUtils {

    /***
     * 写入摘要日志
     */
    public static void sendBizDigestLogger(String logContent) {
        Loggers.BIZ_DIGEST_LOG.info(logContent);
        //写入完成后清空
        LoggerContext.clear();
    }
}
