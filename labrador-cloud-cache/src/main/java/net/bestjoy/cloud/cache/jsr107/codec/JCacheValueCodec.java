package net.bestjoy.cloud.cache.jsr107.codec;

import net.bestjoy.cloud.cache.jsr107.nio.Codec;

import java.nio.ByteBuffer;

/**
 * 缓存Value的编码策略
 */
public class JCacheValueCodec<V> implements Codec<V> {
    private final Codec<V> valueCodec;

    public JCacheValueCodec(Codec<V> valueCodec) {
        this.valueCodec = valueCodec;
    }

    @Override
    public ByteBuffer encode(V value) {
        return valueCodec.encode(value);
    }

    @Override
    public V decode(ByteBuffer buffer) {
        return valueCodec.decode(buffer);
    }
}
