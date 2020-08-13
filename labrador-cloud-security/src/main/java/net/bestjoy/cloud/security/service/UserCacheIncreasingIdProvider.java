package net.bestjoy.cloud.security.service;

import net.bestjoy.cloud.cache.AbstractCacheIncreasingIdProvider;
import net.bestjoy.cloud.security.core.entitiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/***
 * 用户缓存自增id提供类
 * @author ray
 */
@Component
public class UserCacheIncreasingIdProvider extends AbstractCacheIncreasingIdProvider {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Class<User> entityClazzSupport() {
        return User.class;
    }

    @Override
    public String increasingIdPrefix() {
        return "U0";
    }

    @Override
    public RedisTemplate getRedisTemplate() {
        return stringRedisTemplate;
    }
}
