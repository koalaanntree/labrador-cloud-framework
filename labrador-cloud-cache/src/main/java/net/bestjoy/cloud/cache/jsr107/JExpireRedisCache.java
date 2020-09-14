package net.bestjoy.cloud.cache.jsr107;

import io.lettuce.core.SetArgs;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.bestjoy.cloud.cache.jsr107.redis.Lettuce;

import java.util.Map;

/**
 * redis缓存扩展 ： Cache过期时间设置
 */
public class JExpireRedisCache<K, V> extends JRedisCache<K, V> {
    private final static Logger logger = LoggerFactory.getLogger(JExpireRedisCache.class);
    // PX命令操作
    private final SetArgs px;
    // px + nx 命令
    private final SetArgs pxNx;
    // px + xx 命令
    private final SetArgs pxXx;

    public JExpireRedisCache(JCacheManager cacheManager, JCacheConfiguration<K, V> configuration) {
        super(cacheManager, configuration);
        // time to live ,以秒为单位，返回给定Key的剩余生成时间
        long ttl = configuration.getExpiryForUpdateTimeUnit().toMillis(configuration.getExpiryForUpdate());
        this.px = SetArgs.Builder.px(ttl);
        this.pxNx = SetArgs.Builder.px(ttl).nx();
        this.pxXx = SetArgs.Builder.px(ttl).xx();
    }

    @Override
    public void put(K key, V value) {
        redis.set(codec.encodeKey(key), codec.encodeValue(value), px);
    }

    @Override
    public V getAndPut(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        return Lettuce.ok(redis.set(codec.encodeKey(key), codec.encodeValue(value), pxNx));
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(K key, V value) {
        return Lettuce.ok(redis.set(codec.encodeKey(key), codec.encodeValue(value), pxXx));
    }

    @Override
    public V getAndReplace(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
