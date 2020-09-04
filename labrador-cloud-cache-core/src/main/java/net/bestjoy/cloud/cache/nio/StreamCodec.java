package net.bestjoy.cloud.cache.nio;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 定义流输入、输出 的编码和解码
 * PoJo编码为输出流
 * 输入流解码为PoJo
 * 主要用来做网络传输序列化，比如Json转换
 */
public interface StreamCodec<T> {
    void encode(T value, OutputStream stream) throws IOException;

    T decode(InputStream stream) throws IOException;
}
