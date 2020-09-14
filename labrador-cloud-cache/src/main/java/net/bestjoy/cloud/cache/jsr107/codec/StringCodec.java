package net.bestjoy.cloud.cache.jsr107.codec;

import net.bestjoy.cloud.cache.jsr107.nio.Codec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符串编码器
 */
public class StringCodec implements Codec<String> {
    /**
     * NULL字符串占位符
     */
    private static final byte NULL_PLACEHOLDER = 127;
    private static final ByteBuffer NULL = ByteBuffer.wrap(new byte[]{NULL_PLACEHOLDER});
    private final Charset charset;

    public StringCodec() {
        this(StandardCharsets.UTF_8);
    }

    public StringCodec(Charset charset) {
        this.charset = charset;
    }

    private static boolean isNull(ByteBuffer buffer) {
        return (buffer == null) || (buffer.remaining() == 1 && buffer.get(0) == NULL_PLACEHOLDER);
    }

    @Override
    public ByteBuffer encode(String value) {
        return (value == null) ? NULL.duplicate() : charset.encode(value);
    }

    @Override
    public String decode(ByteBuffer buffer) {
        if (isNull(buffer)) {
            return null;
        }
        if (buffer.hasArray()) {
            return new String(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining(), charset);
        }
        return charset.decode(buffer).toString();
    }
}
