package net.bestjoy.cloud.cache.codec;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.bestjoy.cloud.cache.nio.BufferAllocationPredictor;
import net.bestjoy.cloud.cache.nio.Codec;
import net.bestjoy.cloud.cache.ImmutableGeneratedCacheKey;
import net.bestjoy.cloud.cache.nio.AbstractStreamCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

/**
 * 键值编码策略
 * <ul>
 *     <li>1. 该编码策略实现Codec接口</li>
 *     <li>2. 该编码策略只处理ImmutableGeneratedCacheKey类型的key值编码</li>
 * </ul>
 */
public abstract class ImmutableGeneratedCacheKeyCodec implements Codec<ImmutableGeneratedCacheKey> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 根据key类型的规范名称
     * 生成ImmutableGeneratedCacheKeyCodec实例
     *
     * <ul>单值
     *   <li>1.String类型，keyTypeCanonicalName：ava.lang.String</li>
     *   <li>2.Integer类型，keyTypeCanonicalName：java.lang.Integer</li>
     *   <li>3.Long类型，keyTypeCanonicalName：java.lang.Long</li>
     *   <p>[注意]： 不允许使用PoJo作为Key值</p>
     * </ul>
     * <ul>未传值
     *     <li> keyTypeCanonicalName：()</li>
     * </ul>
     *
     * <ul>多值
     *    <li>keyTypeCanonicalName：全限定类名的数组</li>
     * </ul>
     */
    public static Codec<ImmutableGeneratedCacheKey> create(String... keyTypeCanonicalName) throws UnsupportedEncodingException {
        Objects.requireNonNull(keyTypeCanonicalName, "keyTypeCanonicalName不能为空");

        switch (keyTypeCanonicalName.length) {
            case 1: {
                switch (keyTypeCanonicalName[0]) {
                    case "java.lang.String": {
                        return new SingletonKeyCodec(new StringCodec());
                    }
                    case "java.lang.Integer": {
                        return SingletonKeyCodec.INTEGER_KEY_CODEC;
                    }
                    case "java.lang.Long": {
                        return SingletonKeyCodec.LONG_KEY_CODEC;
                    }
                    default: {
                        throw new UnsupportedEncodingException("不允许的Key值类型[" + keyTypeCanonicalName[0] + "],无法编码");
                    }
                }
            }
            case 0: {
                return NoKeyCodec.INSTANCE;
            }
            default: {
                return new ArrayKeyCodec(keyTypeCanonicalName);
            }
        }
    }

    /**
     * 如果没有指定Key时的Key生成策略
     *
     * @see ImmutableGeneratedCacheKey#empty()
     */
    private static class NoKeyCodec implements Codec<ImmutableGeneratedCacheKey> {
        static final NoKeyCodec INSTANCE = new NoKeyCodec();

        @Override
        public ByteBuffer encode(ImmutableGeneratedCacheKey value) {
            return ByteBuffers.empty();
        }

        @Override
        public ImmutableGeneratedCacheKey decode(ByteBuffer buffer) {
            return ImmutableGeneratedCacheKey.empty();
        }
    }

    /**
     * 单键生成策略
     */
    @SuppressWarnings("unchecked")
    private static class SingletonKeyCodec implements Codec<ImmutableGeneratedCacheKey> {
        static final SingletonKeyCodec INTEGER_KEY_CODEC = new SingletonKeyCodec(PrimitiveCodec.INTEGER);
        static final SingletonKeyCodec LONG_KEY_CODEC = new SingletonKeyCodec(PrimitiveCodec.LONG);

        // 定义key值编码器，具体实现由各编码器实现
        private final Codec keyCodec;

        public SingletonKeyCodec(Codec keyCodec) {
            this.keyCodec = keyCodec;
        }

        @Override
        public ByteBuffer encode(ImmutableGeneratedCacheKey value) {
            return keyCodec.encode(value.getKey(0));
        }

        @Override
        public ImmutableGeneratedCacheKey decode(ByteBuffer buffer) {
            return ImmutableGeneratedCacheKey.of(keyCodec.decode(buffer));
        }
    }

    /**
     * 多个key的键值生成策略
     */
    private static class ArrayKeyCodec extends AbstractStreamCodec<ImmutableGeneratedCacheKey> {
        private final JavaType[] types;

        private ArrayKeyCodec(String... canonicalName) {
            super(new BufferAllocationPredictor());
            this.types = Arrays.stream(canonicalName)
                    .map(OBJECT_MAPPER.getTypeFactory()::constructFromCanonical)
                    .toArray(JavaType[]::new);
        }

        @Override
        public void encode(ImmutableGeneratedCacheKey value, OutputStream stream) throws IOException {
            OBJECT_MAPPER.writeValue(stream, value.getKeys());
        }

        @Override
        public ImmutableGeneratedCacheKey decode(InputStream stream) throws IOException {
            ArrayNode arrayNode = OBJECT_MAPPER.readValue(stream, ArrayNode.class);
            Object[] objectArray = new Object[arrayNode.size()];
            for (int i = 0; i < arrayNode.size(); i++) {
                objectArray[i] = OBJECT_MAPPER.convertValue(arrayNode.get(i), types[i]);
            }
            return ImmutableGeneratedCacheKey.of(objectArray);
        }
    }

}
