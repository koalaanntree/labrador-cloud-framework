package net.bestjoy.cloud.core.error;

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
 */
public final class BusinessAssert {
    private static final Map<String, Pattern> PATTERNS = new ConcurrentHashMap<>();

    /**
     * 必须为null
     */
    public static void isNull(Object value) {
        if (value == null) {
            return;
        }
        throwIllegalArgumentException();
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


    public static void isNull(Object value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }


    public static void notNull(Object value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value != null) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 不能为null
     */
    public static void notNull(Object value) {

        if (value != null) {
            return;
        }
        throwIllegalArgumentException();
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
        if (StringUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void isEmpty(String value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 不能为空字符串，且不能为null
     */
    public static void notEmpty(String value) {
        if (!StringUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void notEmpty(String value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (!StringUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 空集合，或为null
     */
    public static void isEmpty(Collection<?> value) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void isEmpty(Collection<?> value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 不能为空集合，且不能为null
     */
    public static void notEmpty(Collection<?> value) {
        if (!CollectionUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void notEmpty(Collection<?> value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (!CollectionUtils.isEmpty(value)) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 不能为空白字符串，且不能为null
     */
    public static void notBlank(String value) {
        if (value != null && !value.trim().isEmpty()) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void notBlank(String value, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value != null && !value.trim().isEmpty()) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须相等，或为null
     */
    public static void eq(Object value, Object that) {
        if (value == null || value.equals(that)) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void eq(Object value, Object that, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.equals(that)) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须不相等，或为null
     */
    public static void ne(Object value, Object that) {
        if (value == null || !value.equals(that)) {
            return;
        }

        throwIllegalArgumentException();
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

    public static void ne(Object value, Object that, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || !value.equals(that)) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须小于，或为null
     */
    public static <T extends Comparable<T>> void lt(T value, T that) {
        if (value == null || value.compareTo(that) < 0) {
            return;
        }

        throwIllegalArgumentException();
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

    public static <T extends Comparable<T>> void lt(T value, T that, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.compareTo(that) < 0) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须小于或等于，或为null
     */
    public static <T extends Comparable<T>> void lte(T value, T that) {
        if (value == null || value.compareTo(that) <= 0) {
            return;
        }
        throwIllegalArgumentException();
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

    public static <T extends Comparable<T>> void lte(T value, T that, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.compareTo(that) <= 0) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须大于，或为null
     */
    public static <T extends Comparable<T>> void gt(T value, T that) {
        if (value == null || value.compareTo(that) > 0) {
            return;
        }

        throwIllegalArgumentException();
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

    public static <T extends Comparable<T>> void gt(T value, T that, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.compareTo(that) > 0) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须大于或等于，或为null
     */
    public static <T extends Comparable<T>> void gte(T value, T that) {
        if (value == null || value.compareTo(that) >= 0) {
            return;
        }

        throwIllegalArgumentException();
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

    public static <T extends Comparable<T>> void gte(T value, T that, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.compareTo(that) >= 0) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 必须在最大最小值之间（含），或为null
     */
    public static <T extends Comparable<T>> void between(T value, T min,
                                                         T max) {
        if (value == null || (value.compareTo(min) >= 0 && value.compareTo(max) <= 0)) {
            return;
        }

        throwIllegalArgumentException();
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

    public static <T extends Comparable<T>> void between(T value, T min, T max, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || (value.compareTo(min) >= 0 && value.compareTo(max) <= 0)) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 字符串长度必须小于最大值（含），或为null
     */
    public static void maxlength(String value, int max) {
        if (value == null || value.length() <= max) {
            return;
        }

        throwIllegalArgumentException();
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

    public static void maxlength(String value, int max, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.length() <= max) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 字符串长度必须大于最小值（含），或为null
     */
    public static void minlength(String value, int min) {
        if (value == null || value.length() >= min) {
            return;
        }

        throwIllegalArgumentException();
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

    public static void minlength(String value, int min, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || value.length() >= min) {
            return;
        }
        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 字符串长度必须在最大值最小值之间（含），或为null
     */
    public static void length(String value, int min, int max) {
        if (value == null || (min <= value.length() && value.length() <= max)) {
            return;
        }
        throwIllegalArgumentException();
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

    public static void length(String value, int min, int max, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || (min <= value.length() && value.length() <= max)) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 值必须在给定集合中，或为null
     */
    public static void in(Object value, Collection<?> collection) {
        if (value == null || collection.contains(value)) {
            return;
        }

        throwIllegalArgumentException();
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

    public static void in(Object value, Collection<?> collection, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || collection.contains(value)) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    /**
     * 字符串必须匹配正则表达式，或为null
     */
    public static void pattern(String value, Pattern regex) {
        if (value == null || regex.matcher(value).matches()) {
            return;
        }

        throwIllegalArgumentException();
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
        Pattern compiled = PATTERNS.get(regex);
        if (compiled == null) {
            PATTERNS.put(regex, compiled = Pattern.compile(regex));
        }
        pattern(value, compiled);
    }

    public static void pattern(String value, Pattern regex, ErrorCodeAndMessage errorCodeAndMessage) {
        if (value == null || regex.matcher(value).matches()) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
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

    public static void pattern(String value, String regex, ErrorCodeAndMessage errorCodeAndMessage) {
        Pattern compiled = PATTERNS.get(regex);
        if (compiled == null) {
            PATTERNS.put(regex, compiled = Pattern.compile(regex));
        }

        pattern(value, compiled, errorCodeAndMessage);
    }

    /**
     * 主键必须大于0，且不为null
     */
    public static void isPrimaryKey(Number key) {
        if (key != null && key.longValue() > 0) {
            return;
        }

        throwIllegalArgumentException();
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

    public static void isPrimaryKey(Number key, ErrorCodeAndMessage errorCodeAndMessage) {
        if (key != null && key.longValue() > 0) {
            return;
        }

        throwIllegalArgumentException(errorCodeAndMessage);
    }

    public static void checkUrn(String urn) {
        if (StringUtils.isNotBlank(urn) && BusinessUrns.isUrn(urn)) {
            return;
        }
        throwIllegalArgumentException("urn不合法");
    }


    public static void throwIllegalArgumentException() {
        throw new BusinessException(Errors.Biz.ILLEGAL_ARGUMENT_ERROR);
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
        throwIllegalArgumentException();
    }

    public static void throwIllegalArgumentException(ErrorCodeAndMessage errorCodeAndMessage) {
        if (errorCodeAndMessage == null) {
            throwIllegalArgumentException();
        }

        throw new BusinessException(errorCodeAndMessage);
    }
}
