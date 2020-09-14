package net.bestjoy.cloud.cache.jsr107;

import java.util.function.Function;

public class CacheValueHolder<K, V> implements Function<K, V> {
    private final Cache<K, V> cache;
    private final Function<V, ? extends CacheValue<V>> factory;
    private volatile CacheValue<V> cacheValue;

    public CacheValueHolder(Cache<K, V> cache, Function<V, ? extends CacheValue<V>> factory) {
        this.cache = cache;
        this.factory = factory;
    }

    @Override
    public V apply(K k) {
        cacheValue = cache.getValue(k, factory);
        return cacheValue == null ? null : cacheValue.get();
    }

    public CacheValue<V> getValue() {
        return cacheValue;
    }
}
