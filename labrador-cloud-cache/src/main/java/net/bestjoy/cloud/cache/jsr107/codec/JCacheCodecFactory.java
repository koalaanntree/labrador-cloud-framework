package net.bestjoy.cloud.cache.jsr107.codec;

import io.lettuce.core.codec.RedisCodec;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheConfiguration;

import java.io.UnsupportedEncodingException;

/**
 * 获取缓存编码
 * <p>
 * 返回RedisCodec ，基于lettuce
 */
@FunctionalInterface
public interface JCacheCodecFactory {
    <K, V> RedisCodec<K, V> getCodec(JCacheConfiguration<K, V> configuration) throws UnsupportedEncodingException;
}
