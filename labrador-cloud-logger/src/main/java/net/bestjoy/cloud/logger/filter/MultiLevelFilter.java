package net.bestjoy.cloud.logger.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Arrays;
import java.util.Objects;

/**
 * 日志多级别输出
 */
public class MultiLevelFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private Level[] levels;


    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        } else {
            return has(levels, event.getLevel()) ? this.onMatch : this.onMismatch;
        }
    }

    /**
     * 判断数组中是否包含指定数据
     */
    public static boolean has(Object[] arrays, Object findObj) {
        if (arrays == null) {
            return false;
        }

        for (Object obj : arrays) {
            if (Objects.equals(obj, findObj)) {
                return true;
            }
        }

        return false;
    }


    public void setLevel(String level) {
        String[] levelStrs = level.split(",");
        levels = Arrays.stream(levelStrs).map(Level::valueOf).toArray(Level[]::new);
    }

    @Override
    public void start() {
        if (this.levels != null) {
            super.start();
        }

    }
}
