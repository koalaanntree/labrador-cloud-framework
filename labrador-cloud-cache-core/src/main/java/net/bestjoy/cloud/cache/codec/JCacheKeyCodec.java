package net.bestjoy.cloud.cache.codec;


import net.bestjoy.cloud.cache.nio.Codec;

/**
 * 缓存Key的编码策略， 继承PrefixCodec
 * <p>
 * 本身不实现encode和decode，由PrefixCodec提供
 * 也就是说：JCacheKeyCodec均会有前缀处理.
 * 在{@link AbstractJCacheCodecFactory}中，我们看到默认设置的前缀就是CacheName,而分隔符为":"
 * </p>
 */
public class JCacheKeyCodec<K> extends PrefixCodec<K> {
    /**
     * 缓存名
     */
    private final String cacheName;

    /**
     * 分隔符
     */
    private final String separator;

    public JCacheKeyCodec(String cacheName, String separator, Codec<K> keyCodec) {
        super(keyCodec, cacheName + separator);
        this.cacheName = cacheName;
        this.separator = separator;
    }

    public String getCacheName() {
        return cacheName;
    }

    public String getSeparator() {
        return separator;
    }
}

