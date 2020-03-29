package net.bestjoy.cloud.cache;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 默认的缓存id生成器，全局递增id
 *
 * @author ray
 */
public class DefaultCacheIdGenerator extends AbstractCacheIncreasingIdProvider {

    private RedisTemplate redisTemplate;

    private DefaultCacheIdGenerator(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Class<?> entityClazzSupport() {
        return DefaultCacheIdGenerator.class;
    }

    @Override
    public String increasingIdPrefix() {
        return "";
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
}
