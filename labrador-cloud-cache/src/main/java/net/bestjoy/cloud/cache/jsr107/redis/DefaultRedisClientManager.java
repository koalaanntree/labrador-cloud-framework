package net.bestjoy.cloud.cache.jsr107.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheRedisProps;

import javax.cache.CacheException;
import java.net.URI;
import java.util.Objects;
import java.util.Properties;

/**
 * 默认redis工厂
 * 获取lettuce客户端
 */
public class DefaultRedisClientManager implements RedisClientManager<RedisClient> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRedisClientManager.class);
    private RedisURI redisURI;

    public DefaultRedisClientManager() {
        logger.debug(">>>>>>初始化RedisClientManager>>>>>>");
    }

    @Override
    public RedisClient getRedisClient(Properties properties) {
        return RedisClient.create(getRedisURI(properties));
    }

    @Override
    public URI getURI(Properties properties) {
        return getRedisURI(properties).toURI();
    }


    public RedisURI getRedisURI(Properties properties) {
        return Objects.isNull(this.redisURI) ? createRedisURI(properties) : this.redisURI;
    }

    public RedisURI createRedisURI(Properties properties) {
        if (Objects.isNull(properties)) {
            throw new CacheException("必须指定缓存配置");
        }
        if (StringUtils.isBlank(JCacheRedisProps.getHost(properties))) {
            throw new CacheException("必须配置Redis的host属性,配置项名称为host");
        }
        String password = JCacheRedisProps.getPassword(properties);
        return StringUtils.isBlank(password) ?
                RedisURI.builder()
                        .withHost(JCacheRedisProps.getHost(properties))
                        .withPort(JCacheRedisProps.getPort(properties))
                        .withDatabase(JCacheRedisProps.getDataBase(properties))
                        .build()
                :
                RedisURI.builder()
                        .withHost(JCacheRedisProps.getHost(properties))
                        .withPort(JCacheRedisProps.getPort(properties))
                        .withDatabase(JCacheRedisProps.getDataBase(properties))
                        .withPassword(password)
                        .build();
    }
}
