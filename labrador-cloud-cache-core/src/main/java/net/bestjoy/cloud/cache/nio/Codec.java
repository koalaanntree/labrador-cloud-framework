package net.bestjoy.cloud.cache.nio;

import java.nio.ByteBuffer;

/**
 * 定义基本缓存编码、解码 方法
 * <p>
 * 支持：
 * 1. <T> 泛型编码，一般该泛型为一个PoJo,编码后返回ByteBuffer
 * <p>
 * 2. ByteBuffer 解码为PoJo
 */
public interface Codec<T> {
    ByteBuffer encode(T value);

    T decode(ByteBuffer buffer);
}
