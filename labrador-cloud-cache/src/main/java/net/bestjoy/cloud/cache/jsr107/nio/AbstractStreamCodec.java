package net.bestjoy.cloud.cache.jsr107.nio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

/**
 * 编码器-抽象类
 * 提供了PoJo与ByteBuffer的基本 编码和解码 --> 转为stream
 * 即： 本质上所有的编码和解码均按照stream形式进行，提高性能,而且后续也好做压缩等更丰富的存储方案
 * <p>
 * 这里面有一个重要的类：
 *
 * <p>
 * 这个是一个非常有意思的ByteBuffer大小预测算法，减少ByteBuffer不必要的扩容以提高读写性能
 */
public abstract class AbstractStreamCodec<T> implements Codec<T>, StreamCodec<T> {
    protected final BufferAllocationPredictor predictor;

    protected AbstractStreamCodec(BufferAllocationPredictor predictor) {
        this.predictor = predictor;
    }

    @Override
    public ByteBuffer encode(T value) {
        try (ByteBufferOutputStream stream = new ByteBufferOutputStream(predictor.getPredicted())) {
            encode(value, stream);
            ByteBuffer byteBuffer = stream.getByteBuffer();
            byteBuffer.flip();
            predictor.accept(byteBuffer.remaining());
            return byteBuffer;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public T decode(ByteBuffer buffer) {
        if (buffer == null) {
            return null;
        }
        try (ByteBufferInputStream stream = new ByteBufferInputStream(buffer)) {
            return decode(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
