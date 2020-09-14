package net.bestjoy.cloud.cache.jsr107;

import java.util.Objects;

/**
 * 缓存值（不可变）
 */
public class ImmutableCacheValue<V> implements CacheValue<V> {
    protected final V value;

    protected ImmutableCacheValue(V value) {
        this.value = value;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ImmutableCacheValue) {
            ImmutableCacheValue<?> that = (ImmutableCacheValue<?>) o;
            return Objects.equals(that.value, this.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }
}
