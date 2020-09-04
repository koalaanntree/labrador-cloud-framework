package net.bestjoy.cloud.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import net.bestjoy.cloud.cache.configuration.JCacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 高级Redis缓存
 * 包括分布式缓存（redis） + 内存缓存（caffeine）
 * 同事还包括缓存过期策略
 */
public class JCache<K, V> implements Cache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(JCache.class);
    // 该缓存的cacheManager
    private final JCacheManager cacheManager;
    // 该缓存的配置
    private final JCacheConfiguration<K, V> configuration;
    // Redis缓存Cache
    private final JRedisCache<K, V> redisCache;
    //内存缓存 - caffeine
    protected final com.github.benmanes.caffeine.cache.Cache<K, V> inMemoryCache;


    @SuppressWarnings("unchecked")
    public JCache(JCacheManager cacheManager, JCacheConfiguration<K, V> configuration) {
        this.cacheManager = cacheManager;
        this.configuration = configuration;
        this.redisCache = cacheManager.newRedisCache(configuration);
        Caffeine caffeine = Caffeine.newBuilder();
        if (configuration.getInMemoryCacheMaxEntry() > 0) {
            caffeine.maximumSize(configuration.getInMemoryCacheMaxEntry());
        }
        // 设置redis过期时间
        long expiryForUpdate = configuration.getExpiryForUpdate() > 0 ?
                configuration.getExpiryForUpdateTimeUnit().toMillis(configuration.getExpiryForUpdate()) :
                -1;
        // 设置caffeine过期时间
        long inMemoryCacheExpiryForUpdate = configuration.getInMemoryCacheExpiryForUpdate() > 0 ?
                configuration.getInMemoryCacheExpiryForUpdateTimeUnit().toMillis(configuration.getInMemoryCacheExpiryForUpdate()) :
                -1;

        if (0 < inMemoryCacheExpiryForUpdate && inMemoryCacheExpiryForUpdate < expiryForUpdate) {
            //  如果内存缓存设置的时间 大于0 小于Redis缓存的时间，则内存缓存的过期时间则以设置为准
            caffeine.expireAfterWrite(
                    configuration.getInMemoryCacheExpiryForUpdate(),
                    configuration.getInMemoryCacheExpiryForUpdateTimeUnit());
        } else if (expiryForUpdate > 0) {
            // 如果设置了Redis缓存，但是内存缓存时间没有设置，则以redis的过期时间为准
            caffeine.expireAfterWrite(
                    configuration.getExpiryForUpdate(),
                    configuration.getExpiryForUpdateTimeUnit());
        }
        this.inMemoryCache = caffeine.build();
    }


    public com.github.benmanes.caffeine.cache.Cache<K, V> getInMemoryCache() {

        return inMemoryCache;
    }

    /**
     * 首先从Redis获取缓存，然后放到内存中
     */
    @Override
    public V get(K key) {
        return inMemoryCache.get(key, redisCache::get);
    }


    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        Map<K, V> keyValues = inMemoryCache.getAllPresent(keys);
        if (keyValues.size() == keys.size()) {
            return keyValues;
        }
        return redisCache.getAll(keys);
    }

    @Override
    public boolean containsKey(K key) {
        return redisCache.containsKey(key);
    }

    @Override
    public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, CompletionListener completionListener) {
        throw new UnsupportedOperationException("该功能严禁使用,小心你的内存");
    }

    @Override
    public void put(K key, V value) {
        redisCache.put(key, value);
        inMemoryCache.invalidate(key);
    }

    @Override
    public V getAndPut(K key, V value) {
        V oldValue = redisCache.getAndPut(key, value);
        inMemoryCache.invalidate(key);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        redisCache.putAll(map);
        inMemoryCache.invalidateAll(map.keySet());
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        if (redisCache.putIfAbsent(key, value)) {
            inMemoryCache.invalidate(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(K key) {
        if (redisCache.remove(key)) {
            inMemoryCache.invalidate(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(K key, V oldValue) {
        if (redisCache.remove(key, oldValue)) {
            inMemoryCache.invalidate(key);
            return true;
        }
        return false;
    }

    @Override
    public V getAndRemove(K key) {
        V value = redisCache.getAndRemove(key);
        inMemoryCache.invalidate(key);
        return value;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        if (redisCache.replace(key, oldValue, newValue)) {
            inMemoryCache.invalidate(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(K key, V value) {
        return false;
    }

    @Override
    public V getAndReplace(K key, V value) {
        V oldValue = redisCache.getAndReplace(key, value);
        inMemoryCache.invalidate(key);
        return oldValue;
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        redisCache.removeAll(keys);
        inMemoryCache.invalidateAll(keys);
    }

    @Override
    public void removeAll() {
        redisCache.removeAll();
        inMemoryCache.invalidateAll();
    }

    @Override
    public void clear() {
        redisCache.clear();
        inMemoryCache.invalidateAll();
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
        T result = redisCache.invoke(key, entryProcessor, arguments);
        inMemoryCache.invalidate(key);
        return result;
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> keys, EntryProcessor<K, V, T> entryProcessor, Object... arguments) {
        Map<K, EntryProcessorResult<T>> result = redisCache.invokeAll(keys, entryProcessor, arguments);
        inMemoryCache.invalidateAll(keys);
        return result;
    }

    @Override
    public String getName() {
        return redisCache.getName();
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public void close() {
        try {
            redisCache.close();
        } finally {
            inMemoryCache.cleanUp();
        }
    }

    @Override
    public boolean isClosed() {
        return redisCache.isClosed();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(redisCache)) {
            return redisCache.unwrap(clazz);
        }
        if (clazz.isInstance(inMemoryCache)) {
            return clazz.cast(inMemoryCache);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        redisCache.registerCacheEntryListener(cacheEntryListenerConfiguration);
    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        redisCache.deregisterCacheEntryListener(cacheEntryListenerConfiguration);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return redisCache.iterator();
    }

    @Override
    public void onRedisConnected() {
        inMemoryCache.invalidateAll();
        logger.debug("onRedisConnected invalidateAll");
    }

    @Override
    public void onRedisDisconnected() {
        inMemoryCache.invalidateAll();
        logger.debug("onRedisDisconnected invalidateAll");
    }

    @Override
    public void onSynchronization() {
        inMemoryCache.invalidateAll();
        logger.debug("onSynchronization invalidateAll");
    }

    @Override
    public void onKeyspaceNotification(ByteBuffer key, ByteBuffer message) {
        K k = redisCache.getCodec().decodeKey(key);
        inMemoryCache.invalidate(k);
        logger.debug("onKeyspaceNotification invalidate {}", k);
    }

    @Override
    public V get(K key, Function<? super K, ? extends V> loader) {
        return inMemoryCache.get(key, new CacheValueLoader<>(redisCache, loader));
    }

    @Override
    public CacheValue<V> getValue(K key) {
        return getValue(key, ImmutableCacheValue::new);
    }

    @Override
    public CacheValue<V> getValue(K key, Function<V, ? extends CacheValue<V>> factory) {
        CacheValueHolder<K, V> cacheValueHolder = new CacheValueHolder<>(redisCache, factory);
        V value = inMemoryCache.get(key, cacheValueHolder);
        if (cacheValueHolder.getValue() != null) {
            return cacheValueHolder.getValue();
        }
        return value == null ? null : factory.apply(value);
    }

    @Override
    public String toString() {
        return redisCache.toString();
    }
}
