package net.bestjoy.cloud.core.util;

import net.bestjoy.cloud.error.bean.Errors;
import net.bestjoy.cloud.error.bean.SysException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author LarryYan
 * @date 2018/6/22 15:32
 */
public class Dates {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATETIME_SIMPLE_FORMAT = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_SIMPLE_FORMAT = "yyyyMMdd";

    public static final String DATE_UNDERLINE_FORMAT = "yyyy_MM_dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String TIME_SIMPLE_FORMAT = "HHmmss";

    public static final String DEFAULT_FORMAT = DATETIME_FORMAT;

    /**
     * 获取当前时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取昨天当前时间
     */
    public static Date yesterday() {
        return addDays(new Date(), -1);
    }

    /**
     * 获取明天当前时间
     */
    public static Date tomorrow() {
        return addDays(new Date(), 1);
    }

    /**
     * 获取当前日期天数
     */
    public static int day(Date date) {
        return (int) (date.getTime() / (24 * 3600 * 1000));
    }

    /**
     * 指定时间是否在当前时间之后
     */
    public static boolean isAfterNow(Date date) {
        return date.after(now());
    }

    /**
     * 指定时间是否在当前时间之前
     */
    public static boolean isBeforeNow(Date date) {
        return date.before(now());
    }

    /**
     * 是否是同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }

    }

    /**
     * 是否是同一天
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 添加年
     */
    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    /**
     * 添加月
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    /**
     * 添加周
     */
    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    /**
     * 添加天
     */
    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    /**
     * 添加小时
     */
    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    /**
     * 添加分钟
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    /**
     * 添加秒
     */
    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    /**
     * 添加毫秒
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }


    /**
     * 日期格式化
     */
    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 日期格式化，默认格式
     */
    public static String format(Date date) {
        return new SimpleDateFormat(DEFAULT_FORMAT).format(date);
    }

    /**
     * 转换String为Date
     */
    public static Date parse(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            throw new SysException(
                    Errors.Biz.ILLEGAL_ARGUMENT_ERROR, "日期格式");
        }
    }

    /**
     * 转换String为Date，默认格式
     */
    public static Date parse(String date) {
        return parse(date, DEFAULT_FORMAT);
    }
}
