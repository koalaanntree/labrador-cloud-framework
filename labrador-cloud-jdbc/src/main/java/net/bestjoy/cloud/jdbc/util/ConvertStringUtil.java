package net.bestjoy.cloud.jdbc.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;


public class ConvertStringUtil {
    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     */
    public static String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

    public static String convertToCustomString(final Collection collection, final String replaceChart, final String separator) {
        StringBuilder builder = new StringBuilder();
        for (@SuppressWarnings("unused") Object o : collection) {
            builder.append(replaceChart);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
