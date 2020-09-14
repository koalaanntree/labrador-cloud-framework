package net.bestjoy.cloud.cache.jsr107.codec;

import net.bestjoy.cloud.cache.jsr107.nio.Codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Java 原始数据类型编码器
 * <p>
 * 只需要支持Long和Integer
 */
public class PrimitiveCodec<T> implements Codec<T> {
    public static final PrimitiveCodec<Integer> INTEGER = new PrimitiveCodec<>(Integer::parseInt);
    public static final PrimitiveCodec<Long> LONG = new PrimitiveCodec<>(Long::parseLong);

    private final Function<String, T> parse;

    public PrimitiveCodec(Function<String, T> parse) {
        this.parse = parse;
    }

    @Override
    public ByteBuffer encode(T value) {
        return (value == null) ? ByteBuffers.empty() : ByteBuffers.from(value.toString(), StandardCharsets.US_ASCII);
    }

    @Override
    public T decode(ByteBuffer buffer) {
        return decode(buffer, null);
    }

    public T decode(ByteBuffer buffer, T defaultValue) {
        return ByteBuffers.isEmpty(buffer) ? defaultValue : parse.apply(ByteBuffers.toString(buffer, StandardCharsets.US_ASCII));
    }
}
