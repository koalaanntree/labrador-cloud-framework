package cache;


import cache.entity.User;
import net.bestjoy.cloud.cache.jsr107.ImmutableGeneratedCacheKey;
import net.bestjoy.cloud.cache.jsr107.JCache;
import net.bestjoy.cloud.cache.jsr107.configuration.JCacheRedisProps;
import net.bestjoy.cloud.cache.jsr107.configuration.MutableJCacheConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class JRedisCacheTest {

    private CachingProvider cachingProvider;
    private Properties properties;
    private CacheManager cacheManager;

    @Before
    public void init() {
        cachingProvider = Caching.getCachingProvider();
        properties = JCacheRedisProps.builder().withHost("192.168.70.131").build();
        cacheManager = cachingProvider.getCacheManager(
                JCacheRedisProps.getLettuceURI(properties),
                Thread.currentThread().getContextClassLoader(),
                properties);
    }

    @After
    public void destroy() {
        cacheManager.close();
        cachingProvider.close();
    }


    /**
     * 最简单的流程:
     * 第一步：
     * 获取Provider --> 构建CacheManger属性 --> 使用Provider和Properties构建一个CacheManager
     * 第二步： 构建Cache配置-JCacheConfiguration，比如Cache名称，过期时间，K-V类型
     * 第三步： 根据Configuration ，由CacheManager 去生成一个Cache
     * 第四步： 使用该Cache去操作内容
     */
    @Test
    public void testSimpleCache() {
        MutableJCacheConfiguration<ImmutableGeneratedCacheKey, String> configuration = new MutableJCacheConfiguration<>();
        configuration.setInMemoryCacheEnabled(false);
        configuration.setName("simpleCache");
        configuration.setExpiryForUpdate(-1);
        configuration.setValueTypeCanonicalName(String.class.getCanonicalName());
        String[] test = {String.class.getCanonicalName()};
        configuration.setKeyTypeCanonicalName(test);
        Cache<ImmutableGeneratedCacheKey, String> simpleCache = cacheManager.createCache("simpleCache", configuration);
        simpleCache.put(ImmutableGeneratedCacheKey.of("testKey"), "testValue");
        //获取数据
        String value = simpleCache.get(ImmutableGeneratedCacheKey.of("testKey"));
        System.out.println(value);
    }

    @Test
    public void testNewManyCacheByOneCacheManager() {
        // 配置第一个Cache
        MutableJCacheConfiguration<ImmutableGeneratedCacheKey, String> configuration1 = new MutableJCacheConfiguration<>();
        configuration1.setInMemoryCacheEnabled(false);
        configuration1.setName("simpleCache1");
        configuration1.setExpiryForUpdate(-1);
        configuration1.setValueTypeCanonicalName(String.class.getCanonicalName());
        String[] keyCanonicalName = {String.class.getCanonicalName()};
        configuration1.setKeyTypeCanonicalName(keyCanonicalName);
        // 创建第一个Cache
        Cache<ImmutableGeneratedCacheKey, String> simpleCache1 = cacheManager.createCache(configuration1.getName(), configuration1);
        simpleCache1.put(ImmutableGeneratedCacheKey.of("testKey"), "testValue");
        //获取数据
        String value1 = simpleCache1.get(ImmutableGeneratedCacheKey.of("testKey"));
        System.out.println(value1);

        // 配置第二个Cache
        MutableJCacheConfiguration<ImmutableGeneratedCacheKey, String> configuration2 = new MutableJCacheConfiguration<>();
        configuration2.setInMemoryCacheEnabled(false);
        configuration2.setName("simpleCache2");
        configuration2.setValueTypeCanonicalName(String.class.getCanonicalName());
        configuration2.setKeyTypeCanonicalName(keyCanonicalName);
        // 创建第二个Cache
        Cache<ImmutableGeneratedCacheKey, String> simpleCache2 = cacheManager.createCache(configuration2.getName(), configuration2);
        simpleCache2.put(ImmutableGeneratedCacheKey.of("testKey"), "testValue");
        //获取数据
        String value2 = simpleCache2.get(ImmutableGeneratedCacheKey.of("testKey"));
        System.out.println(value2);

        // 配置第三个Cache
        MutableJCacheConfiguration<ImmutableGeneratedCacheKey, User> configuration3 = new MutableJCacheConfiguration<>();
        configuration3.setInMemoryCacheEnabled(false);
        configuration3.setName("simpleCache3");
        configuration3.setValueTypeCanonicalName(User.class.getCanonicalName());
        configuration3.setKeyTypeCanonicalName(keyCanonicalName);
        // 创建第三个Cache
        Cache<ImmutableGeneratedCacheKey, User> simpleCache3 = cacheManager.createCache(configuration3.getName(), configuration3);
        User user = new User();
        user.setId(1L);
        user.setName("Joy");
        simpleCache3.put(ImmutableGeneratedCacheKey.of("testKey"), user);
        //获取数据
        User value3 = simpleCache3.get(ImmutableGeneratedCacheKey.of("testKey"));
        System.out.println(value3);
    }

    @Test
    public void testExpireTime() {
        MutableJCacheConfiguration<ImmutableGeneratedCacheKey, String> configuration = new MutableJCacheConfiguration<>();
        configuration.setInMemoryCacheEnabled(false);
        configuration.setName("simpleCache");
        configuration.setExpiryForUpdate(30);
        configuration.setExpiryForUpdateTimeUnit(TimeUnit.SECONDS);
        configuration.setValueTypeCanonicalName(String.class.getCanonicalName());
        String[] test = {String.class.getCanonicalName()};
        configuration.setKeyTypeCanonicalName(test);
        Cache<ImmutableGeneratedCacheKey, String> simpleCache = cacheManager.createCache("simpleCache", configuration);
        simpleCache.put(ImmutableGeneratedCacheKey.of("testKey-ex"), "testValue-ex");
        //获取数据
        String value = simpleCache.get(ImmutableGeneratedCacheKey.of("testKey-ex"));
        System.out.println(value);
    }


    @Test
    public void testJCache() throws InterruptedException {
        MutableJCacheConfiguration<ImmutableGeneratedCacheKey, String> configuration = new MutableJCacheConfiguration<>();

        configuration.setName("JCache");
        configuration.setExpiryForUpdate(30);
        configuration.setExpiryForUpdateTimeUnit(TimeUnit.SECONDS);
        configuration.setValueTypeCanonicalName(String.class.getCanonicalName());
        String[] canonical = {String.class.getCanonicalName()};
        configuration.setKeyTypeCanonicalName(canonical);
        Cache<ImmutableGeneratedCacheKey, String> simpleCache = cacheManager.createCache("JCache", configuration);

        simpleCache.put(ImmutableGeneratedCacheKey.of("testKey-ex"), "testValue-ex");
        //获取数据
        String value = simpleCache.get(ImmutableGeneratedCacheKey.of("testKey-ex"));
        System.out.println(value);

        JCache test = (JCache) simpleCache;
        com.github.benmanes.caffeine.cache.Cache<ImmutableGeneratedCacheKey, String> i = test.getInMemoryCache();
        String cacheValue = i.getIfPresent(ImmutableGeneratedCacheKey.of("testKey-ex"));
        System.out.println(cacheValue);


        Thread.sleep(30000);
        String cacheValue1 = i.getIfPresent(ImmutableGeneratedCacheKey.of("testKey-ex"));
        System.out.println(cacheValue1);
    }


}
