package net.bestjoy.cloud.cache.jsr107;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.bestjoy.cloud.cache.jsr107.codec.ByteBufferCodec;
import net.bestjoy.cloud.cache.jsr107.redis.DefaultRedisClientManager;
import net.bestjoy.cloud.cache.jsr107.spi.JCachingProvider;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存管理器
 */
public class JCacheManager implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(JCacheManager.class);
    // 存储管理该Manager的CachingProvider，以Context相关信息
    private final JCachingProvider cachingProvider;
    // 存储uri 与 cache的映射关系
    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
    // 存储uri
    private final URI uri;
    // 指定classLoader的引用
    private final WeakReference<ClassLoader> classLoaderReference;
    // 指定配置项
    private final Properties properties;
    private volatile boolean isClosed;

    private final StatefulRedisConnection<ByteBuffer, ByteBuffer> redisConnection;

    public JCacheManager(JCachingProvider jCachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        this.cachingProvider = jCachingProvider;
        if (Objects.isNull(uri)) {
            throw new NullPointerException("没有指定CacheManager的uri");
        }
        if (Objects.isNull(classLoader)) {
            throw new NullPointerException("没有指定CacheManager的类加载器");
        }
        this.uri = uri;
        this.classLoaderReference = new WeakReference<>(classLoader);
        this.properties = new Properties();
        if (Objects.nonNull(properties)) {
            for (Object key : properties.keySet()) {
                this.properties.put(key, properties.get(key));
            }
        }
        isClosed = Boolean.FALSE;
        this.redisConnection = new DefaultRedisClientManager()
                .getRedisClient(properties)
                .connect(ByteBufferCodec.INSTANCE);
    }


    @Override
    public CachingProvider getCachingProvider() {
        return cachingProvider;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoaderReference.get();
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V>
    createCache(String cacheName, C configuration) throws IllegalArgumentException {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager已关闭");
        }
        if (Objects.isNull(cacheName)) {
            throw new NullPointerException("cacheName 不能为空");
        }
        if (Objects.isNull(configuration)) {
            throw new NullPointerException("configuration 不能为空");
        }
        synchronized (caches) {
            if (caches.containsKey(cacheName)) {
                throw new CacheException("名称为" + cacheName + "已经存在");
            }
            Cache<K, V> cache = newCache((JCacheConfiguration<K, V>) configuration);
            caches.put(cacheName, cache);
            return cache;
        }
    }

    public <K, V> Cache<K, V> newCache(JCacheConfiguration<K, V> configuration) {
        if (!configuration.isRedisCacheEnabled() & !configuration.isInMemoryCacheEnabled()) {
            throw new IllegalArgumentException("未开启缓存，如果要使用缓存请设置相应配置项[redisCacheEnabled|inMemoryCacheEnabled]");
        }
        if (configuration.isRedisCacheEnabled()) {
            // TODO: isStatisticsEnabled 这个暂未设置,需要用到代理
            if (configuration.isInMemoryCacheEnabled()) {
                return newJCache(configuration);
            }
            return newRedisCache(configuration);
        } else {
            // 如果redis缓存未开启，则只创建内存缓存
            return newInMemoryCache(configuration);
        }
    }

    public <K, V> JRedisCache<K, V> newRedisCache(JCacheConfiguration<K, V> configuration) {
        if (configuration.getExpiryForUpdate() > 0) {
            return new JExpireRedisCache<>(this, configuration);
        } else {
            return new JRedisCache<>(this, configuration);
        }
    }

    public <K, V> JCache<K, V> newJCache(JCacheConfiguration<K, V> configuration) {
        return new JCache<>(this, configuration);
    }

    public <K, V> InMemoryCache<K, V> newInMemoryCache(JCacheConfiguration<K, V> configuration) {
        return new InMemoryCache<>(this, configuration);
    }


    @Override
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager已关闭");
        }
        if (Objects.isNull(keyType)) {
            throw new NullPointerException("keyType不能为空");
        }
        if (Objects.isNull(valueType)) {
            throw new NullPointerException("valueType不能为空");
        }
        synchronized (caches) {
            Cache<?, ?> cache = caches.get(cacheName);
            if (Objects.isNull(cache)) {
                return null;
            } else {
                Configuration<?, ?> configuration = cache.getConfiguration(JCacheConfiguration.class);
                if (Objects.nonNull(configuration.getKeyType()) && configuration.getKeyType().equals(keyType)) {
                    if (Objects.nonNull(configuration.getValueType()) && configuration.getValueType().equals(valueType)) {
                        return (Cache<K, V>) cache;
                    } else {
                        throw new ClassCastException("cacheValue类型错误(" + configuration.getValueType() + "," + valueType + ")");
                    }
                } else {
                    throw new ClassCastException("keyValue类型错误(" + configuration.getValueType() + "," + valueType + ")");
                }
            }
        }
    }

    @Override
    public Cache getCache(String cacheName) {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager已关闭");
        }
        synchronized (caches) {
            return caches.get(cacheName);
        }
    }

    @Override
    public Iterable<String> getCacheNames() {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager已关闭");
        }
        synchronized (caches) {
            Set<String> set = new HashSet<>();
            for (Cache<?, ?> cache : caches.values()) {
                set.add(cache.getName());
            }
            return Collections.unmodifiableSet(set);
        }
    }

    @Override
    public void destroyCache(String cacheName) {
        if (isClosed()) {
            throw new IllegalStateException("CacheManager已关闭");
        }
        if (Objects.isNull(cacheName)) {
            throw new NullPointerException("cacheName不能为空");
        }
        Cache<?, ?> cache;
        synchronized (caches) {
            cache = caches.get(cacheName);
        }

        if (cache != null) {
            cache.close();
        }
    }

    @Override
    public void enableManagement(String cacheName, boolean enabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void enableStatistics(String cacheName, boolean enabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        if (!isClosed()) {
            cachingProvider.releaseCacheManager(getURI(), getClassLoader());
            isClosed = Boolean.TRUE;

            List<Cache<?, ?>> cacheList;
            synchronized (caches) {
                cacheList = new ArrayList<>(caches.values());
                caches.clear();
            }
            for (Cache<?, ?> cache : cacheList) {
                try {
                    cache.close();
                } catch (Exception e) {
                    logger.warn("cache关闭错误:{} ", cache, e);
                }
            }
        }
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        if (cls.isAssignableFrom(getClass())) {
            return cls.cast(this);
        }

        throw new IllegalArgumentException("Unwapping to " + cls + " is not a supported by this implementation");
    }


    void releaseCache(String cacheName) {
        if (Objects.isNull(cacheName)) {
            throw new NullPointerException("cacheName不能为空");
        }
        synchronized (caches) {
            caches.remove(cacheName);
        }
    }

    public <V, K> RedisCodec<K, V> newJCacheCodec(JCacheConfiguration<K, V> configuration) {
        try {
            return configuration.getJCacheCodecFactory().newInstance().getCodec(configuration);
        } catch (InstantiationException | IllegalAccessException | UnsupportedEncodingException e) {
            throw new CacheException(e);
        }
    }


    public RedisCommands<ByteBuffer, ByteBuffer> getRedisCommands() {
        return this.redisConnection.sync();
    }
}
