package net.bestjoy.cloud.cache.jsr107;

import java.util.function.Supplier;

/**
 * 缓存值
 */
public interface CacheValue<V> extends Supplier<V> {
    int hashCode();

    boolean equals(Object o);
}
