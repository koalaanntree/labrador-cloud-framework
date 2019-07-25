package net.bestjoy.cloud.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 定义日志类型
 * @author ray
 */
public interface Loggers {

    /**
     * 摘要日志
     */
    Logger BIZ_DIGEST_LOG = LoggerFactory.getLogger("BIZ_DIGEST_LOG");
}
