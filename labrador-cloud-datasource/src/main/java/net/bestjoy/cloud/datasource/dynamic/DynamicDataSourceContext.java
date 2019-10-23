package net.bestjoy.cloud.datasource.dynamic;

import org.springframework.core.NamedThreadLocal;

/***
 * 多数据源上下文
 * @author ray
 */
public class DynamicDataSourceContext {

    private static final NamedThreadLocal<String> CONCURRENT_LOCK_UP_HOLDER = new NamedThreadLocal<>("concurrent dynamic datasource");

    public static String getDataSourceLookupKey() {
        return CONCURRENT_LOCK_UP_HOLDER.get();
    }

    public static void setDataSourceLookupKey(String dataSourceLookupKey) {
        CONCURRENT_LOCK_UP_HOLDER.set(dataSourceLookupKey);
    }

    public static void clearDataSourceLookupKey() {
        CONCURRENT_LOCK_UP_HOLDER.remove();
    }
}
