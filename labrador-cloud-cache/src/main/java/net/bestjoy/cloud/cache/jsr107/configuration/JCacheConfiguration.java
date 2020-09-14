package net.bestjoy.cloud.cache.jsr107.configuration;

import net.bestjoy.cloud.cache.jsr107.codec.JCacheCodecFactory;

import javax.cache.configuration.Configuration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类 - 对Configuration的扩展
 * <p>
 * [特别注意]： 注意区分Cache与Redis
 * Cache 是JCache对缓存条目操作的抽象，
 * Redis是Cache存储的落脚点，可以这么认为： Cache就是Dao的定义，Redis就是数据库.
 * 这个地方的Configuration是对Cache的配置，比如： 这个Cache名称，K-V类型，过期时间...
 * 一个CacheManager可以创建很多Cache,
 * 这就比如： 一个应用有可能有很多 Dao，而这些Dao共用一个数据库.
 * 而数据库的配置，则应该由CacheManager中的Properties去处理.
 * </p>
 *
 * @see Configuration
 * @see javax.cache.Cache
 * @see javax.cache.CacheManager
 */
public interface JCacheConfiguration<K, V> extends Configuration<K, V> {
    /**
     * 是否值存储： 是
     */
    @Override
    default boolean isStoreByValue() {
        return true;
    }

    /**
     * 缓存名
     */
    String getName();

    /**
     * 缓存键类型名
     */
    String[] getKeyTypeCanonicalName();

    /**
     * 缓存值类型名
     */
    String getValueTypeCanonicalName();

    /**
     * 缓存键编码器
     */
    Class<? extends JCacheCodecFactory> getJCacheCodecFactory();

    /**
     * 过期时间
     */
    long getExpiryForUpdate();

    /**
     * 过期时间单位
     */
    TimeUnit getExpiryForUpdateTimeUnit();

    /**
     * 是否启用Redis缓存
     */
    boolean isRedisCacheEnabled();

    /**
     * 是否启用内存缓存
     */
    boolean isInMemoryCacheEnabled();

    /**
     * 内存缓存最大数量
     */
    int getInMemoryCacheMaxEntry();

    /**
     * 内存缓存过期时间
     */
    long getInMemoryCacheExpiryForUpdate();

    /**
     * 内存缓存过期时间单位
     */
    TimeUnit getInMemoryCacheExpiryForUpdateTimeUnit();

    /**
     * 额外参数
     */
    List<String> getExConfigurations();


    boolean isStatisticsEnabled();

    boolean isManagementEnabled();
}

