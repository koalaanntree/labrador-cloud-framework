package net.bestjoy.cloud.cache;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * Redis缓存扩展
 */
public interface Cache<K, V> extends javax.cache.Cache<K, V> {
    /**
     * 读取/加载缓存值
     */
    V get(K key, Function<? super K, ? extends V> loader);

    /**
     * 读取缓存值，未命中返回null
     */
    CacheValue<V> getValue(K key);

    /**
     * 读取缓存值，未命中返回null
     */
    CacheValue<V> getValue(K key, Function<V, ? extends CacheValue<V>> factory);

    /**
     * Redis连接成功回调
     */
    default void onRedisConnected() {
    }

    /**
     * Redis连接丢失回调
     */
    default void onRedisDisconnected() {
    }

    /**
     * Redis数据同步
     */
    default void onSynchronization() {
    }

    /**
     * Redis键通知回调
     */
    default void onKeyspaceNotification(ByteBuffer key, ByteBuffer message) {
    }
}
