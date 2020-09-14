package net.bestjoy.cloud.cache.jsr107.codec;

import net.bestjoy.cloud.cache.jsr107.configuration.JCacheConfiguration;
import net.bestjoy.cloud.cache.jsr107.nio.Codec;
import net.bestjoy.cloud.cache.jsr107.nio.JacksonCodec;

import java.io.UnsupportedEncodingException;

/**
 * 默认JCache编码器工厂
 */
public class DefaultJCacheCodecFactory extends AbstractJCacheCodecFactory {

    @Override
    @SuppressWarnings("unchecked")
    protected <K, V> Codec<K> newKeyCodec(JCacheConfiguration<K, V> configuration) throws UnsupportedEncodingException {
        return (Codec<K>) ImmutableGeneratedCacheKeyCodec.create(configuration.getKeyTypeCanonicalName());
    }

    @Override
    protected <K, V> Codec<V> newValueCodec(JCacheConfiguration<K, V> configuration) {
        return new JacksonCodec<>(configuration.getValueTypeCanonicalName());
    }
}

