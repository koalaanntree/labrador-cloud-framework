package net.bestjoy.cloud.cache.jsr107.codec;


import io.lettuce.core.codec.RedisCodec;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheConfiguration;
import net.bestjoy.cloud.cache.jsr107.nio.Codec;
import net.bestjoy.cloud.cache.jsr107.redis.RedisCodecAdapter;

import java.io.UnsupportedEncodingException;

/**
 * 默认将我们的缓存编码适配到lettuce的codec上
 */
public abstract class AbstractJCacheCodecFactory implements JCacheCodecFactory {
    @Override
    public <K, V> RedisCodec<K, V> getCodec(JCacheConfiguration<K, V> configuration) throws UnsupportedEncodingException {
        return new RedisCodecAdapter<>(
                new JCacheKeyCodec<>(configuration.getName(), getSeparator(configuration), newKeyCodec(configuration)),
                new JCacheValueCodec<>(newValueCodec(configuration)));
    }

    /**
     * key分隔符生成策略
     * 如果keyTypeCanonicalName没有被指定，则不指定分隔符
     * 如果有值，则用“：”分隔
     * [ps]:为什么用 ： 分割？ 目前一个小原因是因为可以用RedisDesktopManager分组查看，这个分隔符不允许设置
     */
    protected <K, V> String getSeparator(JCacheConfiguration<K, V> configuration) {
        return configuration.getKeyTypeCanonicalName().length == 0 ? "" : ":";
    }

    protected abstract <K, V> Codec<K> newKeyCodec(JCacheConfiguration<K, V> configuration) throws UnsupportedEncodingException;

    protected abstract <K, V> Codec<V> newValueCodec(JCacheConfiguration<K, V> configuration);
}
