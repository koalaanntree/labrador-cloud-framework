package net.bestjoy.cloud.cache;

import java.util.function.Function;

public class CacheValueLoader<K, V> implements Function<K, V> {
    private final Cache<K, V> cache;
    private final Function<? super K, ? extends V> loader;

    public CacheValueLoader(Cache<K, V> cache, Function<? super K, ? extends V> loader) {
        this.cache = cache;
        this.loader = loader;
    }

    @Override
    public V apply(K k) {
        return cache.get(k, loader);
    }
}
