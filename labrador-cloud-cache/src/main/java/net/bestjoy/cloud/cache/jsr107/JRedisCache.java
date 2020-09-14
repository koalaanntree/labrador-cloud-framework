package net.bestjoy.cloud.cache.jsr107;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheConfiguration;
import net.bestjoy.cloud.cache.jsr107.processor.EntryProcessorResultWrapper;
import net.bestjoy.cloud.cache.jsr107.redis.Lettuce;

import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 缓存实现
 * 简单的Redis缓存
 */
public class JRedisCache<K, V> implements Cache<K, V> {
    // 该缓存的cacheManager
    protected final JCacheManager cacheManager;
    // 该缓存的配置
    protected final JCacheConfiguration<K, V> configuration;
    // 缓存编码
    protected final RedisCodec<K, V> codec;
    // 缓存操作
    protected final RedisCommands<ByteBuffer, ByteBuffer> redis;

    public JRedisCache(JCacheManager cacheManager,
                       JCacheConfiguration<K, V> configuration) {
        Objects.requireNonNull(configuration.getName());
        this.cacheManager = cacheManager;
        this.configuration = configuration;
        this.codec = cacheManager.newJCacheCodec(configuration);
        this.redis = cacheManager.getRedisCommands();
    }

    public RedisCodec<K, V> getCodec() {
        return codec;
    }

    @Override
    public V get(K key) {
        return codec.decodeValue(redis.get(codec.encodeKey(key)));
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        if (keys.isEmpty()) {
            return Collections.emptyMap();
        }
        ByteBuffer[] encodedKeys = keys.stream()
                .map(key -> (K) key)
                .map(codec::encodeKey)
                .toArray(ByteBuffer[]::new);
        return redis.mget(encodedKeys).stream()
                .collect(Collectors.toMap(kv -> codec.decodeKey(kv.getKey()),
                        kv -> codec.decodeValue(kv.getValueOrElse(null))));
    }

    @Override
    public boolean containsKey(K key) {
        return redis.exists(codec.encodeKey(key)) > 0;
    }

    @Override
    public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, CompletionListener completionListener) {
        throw new UnsupportedOperationException("该功能严禁使用,小心你的内存");
    }

    @Override
    public void put(K key, V value) {
        redis.set(codec.encodeKey(key), codec.encodeValue(value));
    }

    @Override
    public V getAndPut(K key, V value) {
        return codec.decodeValue(redis.getset(codec.encodeKey(key), codec.encodeValue(value)));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (map.isEmpty()) {
            return;
        }
        Map<ByteBuffer, ByteBuffer> encodedKeyValues = map.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> codec.encodeKey(e.getKey()),
                        e -> codec.encodeValue(e.getValue())));
        redis.mset(encodedKeyValues);
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        return Lettuce.ok(redis.set(codec.encodeKey(key), codec.encodeValue(value), Lettuce.SET_NX));
    }

    @Override
    public boolean remove(K key) {
        return redis.del(codec.encodeKey(key)) > 0;
    }

    @Override
    public boolean remove(K key, V oldValue) {
        throw new UnsupportedOperationException("不提供该支持");
    }

    @Override
    public V getAndRemove(K key) {
        throw new UnsupportedOperationException("不提供该支持");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException("不提供该该支持");
    }

    @Override
    public boolean replace(K key, V value) {
        return Lettuce.ok(redis.set(codec.encodeKey(key), codec.encodeValue(value), Lettuce.SET_XX));
    }

    @Override
    public V getAndReplace(K key, V value) {
        throw new UnsupportedOperationException("不允许获取然后Update操作");
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        if (keys.isEmpty()) {
            return;
        }
        ByteBuffer[] encodedKeys = keys.stream()
                .map(key -> (K) key)
                .map(codec::encodeKey)
                .toArray(ByteBuffer[]::new);
        redis.del(encodedKeys);
    }

    @Override
    public void removeAll() {
        ScanArgs scanArgs = ScanArgs.Builder.matches(getName() + ":*").limit(100);
        ScanCursor scanCursor = ScanCursor.INITIAL;
        while (!scanCursor.isFinished()) {
            KeyScanCursor<ByteBuffer> keyScanCursor = redis.scan(scanCursor, scanArgs);
            if (!keyScanCursor.getKeys().isEmpty()) {
                redis.del(keyScanCursor.getKeys().toArray(new ByteBuffer[0]));
            }
            scanCursor = keyScanCursor;
        }
    }

    @Override
    public void clear() {
        removeAll();
    }

    @Override
    public <C extends Configuration<K, V>> C getConfiguration(Class<C> clazz) {
        if (clazz.isInstance(configuration)) {
            return clazz.cast(configuration);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public <T> T invoke(K key, EntryProcessor<K, V, T> entryProcessor, Object... arguments) throws EntryProcessorException {
        return entryProcessor.process(new MutableCacheEntry<>(this, key), arguments);
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> keys, EntryProcessor<K, V, T> entryProcessor, Object... arguments) {
        Map<K, EntryProcessorResult<T>> results = new LinkedHashMap<>();
        for (K key : keys) {
            try {
                results.put(key, new EntryProcessorResultWrapper<>(invoke(key, entryProcessor, arguments)));
            } catch (RuntimeException e) {
                results.put(key, new EntryProcessorResultWrapper<>(new EntryProcessorException(e)));
            }
        }
        return results;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return clazz.cast(this);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K key, Function<? super K, ? extends V> loader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheValue<V> getValue(K key) {
        return getValue(key, ImmutableCacheValue::new);
    }

    @Override
    public CacheValue<V> getValue(K key, Function<V, ? extends CacheValue<V>> factory) {
        ByteBuffer encodedValue = redis.get(codec.encodeKey(key));
        if (encodedValue == null) {
            return null;
        }
        return factory.apply(codec.decodeValue(encodedValue));
    }
}
