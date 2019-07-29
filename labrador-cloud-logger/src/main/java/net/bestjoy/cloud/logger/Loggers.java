package net.bestjoy.cloud.logger;

import net.bestjoy.cloud.logger.context.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 定义日志类型
 * //todo 工厂模式，选择写入文件还是日志系统
 * @author ray
 */
public interface Loggers {

    /**
     * 摘要日志
     */
    Logger BIZ_DIGEST_LOG = LoggerFactory.getLogger("BIZ_DIGEST_LOG");

    /**
     * 输出摘要日志
     */
    static void sendBizDigestLog() {
        BIZ_DIGEST_LOG.info("");
        LoggerContext.clear();
    }


    /**
     * mq日志
     */
    Logger MQ_DIGEST_LOG = LoggerFactory.getLogger("MQ_DIGEST_LOG");

    static void sendMqDigestLog(String loggerContent) {
        MQ_DIGEST_LOG.info(loggerContent);
        LoggerContext.clear();
    }
}
