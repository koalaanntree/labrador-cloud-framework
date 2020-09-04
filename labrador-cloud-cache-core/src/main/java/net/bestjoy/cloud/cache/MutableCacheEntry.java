package net.bestjoy.cloud.cache;

import javax.cache.Cache;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;

/**
 * 可变键值对(即： 缓存条目)
 * 目的： 结合{@link EntryProcessor} 用于保证K-V操作的原子性
 */
public class MutableCacheEntry<K, V> implements MutableEntry<K, V> {
    private final Cache<K, V> cache;
    private final K key;

    public MutableCacheEntry(Cache<K, V> cache, K key) {
        this.cache = cache;
        this.key = key;
    }

    @Override
    public boolean exists() {
        return cache.containsKey(key);
    }

    @Override
    public void remove() {
        cache.remove(key);
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return cache.get(key);
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return clazz.cast(this);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void setValue(V value) {
        cache.put(key, value);
    }

    @Override
    public String toString() {
        return key + "@" + cache;
    }
}
