package net.bestjoy.cloud.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 全局唯一有序ID单机版，系统时区相关
 * <p>
 * Long.MAX_VALUE 9223372036854775807
 * UniqueId       2017010123595900001
 */
public final class UniqueId {
    private static final long SEQUENCE_MASK = 100000L; // 5位序列号，最大99999
    private volatile long time = 0L;
    private volatile int sequence = 0;

    /**
     * 格式化时间为yyyyMMddHHmmss，并转为long类型
     */
    public static long formatDateTimeAsLong(LocalDateTime dateTime) {
        return dateTime.getYear() * 10000000000L
                + dateTime.getMonthValue() * 100000000L
                + dateTime.getDayOfMonth() * 1000000L
                + dateTime.getHour() * 10000L
                + dateTime.getMinute() * 100L
                + dateTime.getSecond();
    }

    /**
     * 根据毫秒时间戳及序列号，生成ID
     */
    public static Long newUniqueId(long timeMillis, int sequence) {
        return newUniqueId(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault()), sequence);
    }

    /**
     * 根据本地时间，生成ID
     */
    public static Long newUniqueId(LocalDateTime dateTime, int sequence) {
        if (sequence >= SEQUENCE_MASK) {
            throw new IllegalArgumentException();
        }
        return (formatDateTimeAsLong(dateTime) * SEQUENCE_MASK) + sequence;
    }

    /**
     * 根据当前本地时间，生成ID
     */
    public static Long newUniqueId(int sequence) {
        return newUniqueId(LocalDateTime.now(), sequence);
    }

    /**
     * 生成ID，使用synchronized保证线程安全
     */
    public synchronized Long newUniqueId() {
        while (true) {
            long now = formatDateTimeAsLong(LocalDateTime.now());
            if (time != now) {
                // 时间有变化，重置序列号
                time = now;
                sequence = 0;
            } else {
                // 时间没有变化，序列号递增
                sequence++;
                if (sequence >= SEQUENCE_MASK) {
                    // 序列号溢出，自旋
                    continue;
                }
            }
            return (time * SEQUENCE_MASK) + sequence;
        }
    }
}
