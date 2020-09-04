package net.bestjoy.cloud.cache.redis;

import java.net.URI;
import java.util.Properties;

/**
 * Redis 客户端工厂
 * 根据配置文件获取redis配置
 */
public interface RedisClientManager<T> {
    T getRedisClient(Properties properties);

    URI getURI(Properties properties);
}
