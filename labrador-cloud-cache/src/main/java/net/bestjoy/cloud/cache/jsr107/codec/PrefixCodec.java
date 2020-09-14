package net.bestjoy.cloud.cache.jsr107.codec;

import net.bestjoy.cloud.cache.jsr107.nio.Codec;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 前缀编码
 */
public class PrefixCodec<T> implements Codec<T> {
    private final Codec<T> codec;
    private final ByteBuffer prefix;

    public PrefixCodec(Codec<T> codec, ByteBuffer prefix) {
        this.codec = codec;
        this.prefix = prefix;
    }

    public PrefixCodec(Codec<T> codec, String prefix) {
        this(codec, ByteBuffers.fromUtf8(prefix));
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public ByteBuffer getPrefix() {
        return prefix;
    }


    @Override
    public ByteBuffer encode(T value) {
        return ByteBuffers.concat(prefix, codec.encode(value));
    }

    @Override
    public T decode(ByteBuffer buffer) {
        if (Objects.isNull(buffer)) {
            return null;
        }
        if (skipPrefix(buffer)) {
            return codec.decode(buffer);
        }
        throw new IllegalArgumentException("prefix参数错误");
    }

    protected boolean skipPrefix(ByteBuffer buffer) {
        buffer.position(buffer.position() + prefix.remaining());
        return true;
    }

    protected boolean checkAndSkipPrefix(ByteBuffer buffer) {
        for (int i = 0; i < prefix.remaining(); i++) {
            if (buffer.get() != prefix.get(i)) {
                return false;
            }
        }
        return true;
    }
}
