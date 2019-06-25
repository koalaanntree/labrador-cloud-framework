package net.bestjoy.cloud.error.util;

import net.bestjoy.cloud.error.bean.BusinessException;
import net.bestjoy.cloud.error.bean.BusinessUrns;
import net.bestjoy.cloud.error.bean.Errors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 业务断言,主要是进行一些空或者非空判断
 *
 * @author : joy lee
 * @date : 2018-12-28 21:54
 */
public final class BusinessAssert {
    private static final Map<String, Pattern> PATTERNS = new ConcurrentHashMap<>();

    /**
     * 必须为null
     */
    public static void isNull(Object value) {
        isNull(value, null);
    }

    /**
     * 必须为null
     */
    public static void isNull(Object value, String message) {
        if (value == null) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 不能为null
     */
    public static void notNull(Object value) {
        notNull(value, null);
    }

    /**
     * 不能为null
     */
    public static void notNull(Object value, String message) {
        if (value != null) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须为空字符串，或为null
     */
    public static void isEmpty(String value) {
        isEmpty(value, null);
    }

    /**
     * 必须为空字符串，或为null
     */
    public static void isEmpty(String value, String message) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 不能为空字符串，且不能为null
     */
    public static void notEmpty(String value) {
        notEmpty(value, null);
    }

    /**
     * 不能为空字符串，且不能为null
     */
    public static void notEmpty(String value, String message) {
        if (!StringUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 空集合，或为null
     */
    public static void isEmpty(Collection<?> value) {
        isEmpty(value, null);
    }

    /**
     * 空集合，或为null
     */
    public static void isEmpty(Collection<?> value, String message) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 不能为空集合，且不能为null
     */
    public static void notEmpty(Collection<?> value) {
        notEmpty(value, null);
    }

    /**
     * 不能为空集合，且不能为null
     */
    public static void notEmpty(Collection<?> value, String message) {
        if (!CollectionUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 不能为空白字符串，且不能为null
     */
    public static void notBlank(String value) {
        notBlank(value, null);
    }

    /**
     * 不能为空白字符串，且不能为null
     */
    public static void notBlank(String value, String message) {
        if (value != null && !value.trim().isEmpty()) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须相等，或为null
     */
    public static void eq(Object value, Object that) {
        eq(value, that, null);
    }

    /**
     * 必须相等，或为null
     */
    public static void eq(Object value, Object that, String message) {
        if (value == null || value.equals(that)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须不相等，或为null
     */
    public static void ne(Object value, Object that) {
        ne(value, that, null);
    }

    /**
     * 必须不相等，或为null
     */
    public static void ne(Object value, Object that, String message) {
        if (value == null || !value.equals(that)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须小于，或为null
     */
    public static <T extends Comparable<T>> void lt(T value, T that) {
        lt(value, that, null);
    }

    /**
     * 必须小于，或为null
     */
    public static <T extends Comparable<T>> void lt(T value, T that,
                                                    String message) {
        if (value == null || value.compareTo(that) < 0) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须小于或等于，或为null
     */
    public static <T extends Comparable<T>> void lte(T value, T that) {
        lte(value, that, null);
    }

    /**
     * 必须小于或等于，或为null
     */
    public static <T extends Comparable<T>> void lte(T value, T that,
                                                     String message) {
        if (value == null || value.compareTo(that) <= 0) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须大于，或为null
     */
    public static <T extends Comparable<T>> void gt(T value, T that) {
        gt(value, that, null);
    }

    /**
     * 必须大于，或为null
     */
    public static <T extends Comparable<T>> void gt(T value, T that,
                                                    String message) {
        if (value == null || value.compareTo(that) > 0) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须大于或等于，或为null
     */
    public static <T extends Comparable<T>> void gte(T value, T that) {
        gte(value, that, null);
    }

    /**
     * 必须大于或等于，或为null
     */
    public static <T extends Comparable<T>> void gte(T value, T that,
                                                     String message) {
        if (value == null || value.compareTo(that) >= 0) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 必须在最大最小值之间（含），或为null
     */
    public static <T extends Comparable<T>> void between(T value, T min,
                                                         T max) {
        between(value, min, max, null);
    }

    /**
     * 必须在最大最小值之间（含），或为null
     */
    public static <T extends Comparable<T>> void between(T value, T min, T max,
                                                         String message) {
        if (value == null
                || (value.compareTo(min) >= 0 && value.compareTo(max) <= 0)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 字符串长度必须小于最大值（含），或为null
     */
    public static void maxlength(String value, int max) {
        maxlength(value, max, null);
    }

    /**
     * 字符串长度必须小于最大值（含），或为null
     */
    public static void maxlength(String value, int max, String message) {
        if (value == null || value.length() <= max) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 字符串长度必须大于最小值（含），或为null
     */
    public static void minlength(String value, int min) {
        minlength(value, min, null);
    }

    /**
     * 字符串长度必须大于最小值（含），或为null
     */
    public static void minlength(String value, int min, String message) {
        if (value == null || value.length() >= min) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 字符串长度必须在最大值最小值之间（含），或为null
     */
    public static void length(String value, int min, int max) {
        length(value, min, max, null);
    }

    /**
     * 字符串长度必须在最大值最小值之间（含），或为null
     */
    public static void length(String value, int min, int max, String message) {
        if (value == null || (min <= value.length() && value.length() <= max)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 值必须在给定集合中，或为null
     */
    public static void in(Object value, Collection<?> collection) {
        in(value, collection, null);
    }

    /**
     * 值必须在给定集合中，或为null
     */
    public static void in(Object value, Collection<?> collection,
                          String message) {
        if (value == null || collection.contains(value)) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 字符串必须匹配正则表达式，或为null
     */
    public static void pattern(String value, Pattern regex) {
        pattern(value, regex, null);
    }

    /**
     * 字符串必须匹配正则表达式，或为null
     */
    public static void pattern(String value, Pattern regex, String message) {
        if (value == null || regex.matcher(value).matches()) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    /**
     * 字符串必须匹配正则表达式，或为null
     */
    public static void pattern(String value, String regex) {
        pattern(value, regex, null);
    }

    /**
     * 字符串必须匹配正则表达式，或为null
     */
    public static void pattern(String value, String regex, String message) {
        Pattern compiled = PATTERNS.get(regex);
        if (compiled == null) {
            PATTERNS.put(regex, compiled = Pattern.compile(regex));
        }
        pattern(value, compiled, message);
    }

    /**
     * 主键必须大于0，且不为null
     */
    public static void isPrimaryKey(Number key) {
        isPrimaryKey(key, null);
    }

    /**
     * 主键必须大于0，且不为null
     */
    public static void isPrimaryKey(Number key, String message) {
        if (key != null && key.longValue() > 0) {
            return;
        }
        throwIllegalArgumentException(message);
    }

    public static void checkUrn(String urn) {
        if (StringUtils.isNotBlank(urn) && BusinessUrns.isUrn(urn)) {
            return;
        }
        throwIllegalArgumentException("urn不合法");
    }

    /**
     * 抛出参数异常
     *
     * @param message
     */
    public static void throwIllegalArgumentException(String message) {
        if (StringUtils.isNotBlank(message)) {
            throw new BusinessException(Errors.Biz.ILLEGAL_ARGUMENT_ERROR, message);
        }
        throw new BusinessException(Errors.Biz.ILLEGAL_ARGUMENT_ERROR);
    }
}
