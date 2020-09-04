package net.bestjoy.cloud.cache.spi;

import net.bestjoy.cloud.cache.JCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caching-Provider实现
 * 作用： 管理本工具的CacheMangers
 */
public class JCachingProvider implements CachingProvider {
    private static final Logger logger = LoggerFactory.getLogger(JCachingProvider.class);
    private Map<ClassLoader, Map<URI, CacheManager>> cacheManagersByClassLoader;

    public JCachingProvider() {
        logger.debug(">>>>>>初始化JCachingProvider>>>>>>");
        this.cacheManagersByClassLoader = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        logger.debug("uri:{},classLoader:{},properties:{}", uri, classLoader, properties);
        //构建URI，如果uri为空，则获取默认uri（即是本全限定类名）
        URI managerURI = Objects.isNull(uri) ? getDefaultURI() : uri;
        //构造类加载器,如果参数为空，则获取默认类加载器(即本类的加载器)
        ClassLoader managerClassLoader = Objects.isNull(classLoader) ? getDefaultClassLoader() : classLoader;
        //构造配置参数,如果为空则new一个properties对象
        Properties managerProperties = Objects.isNull(properties) ? new Properties() : properties;

        //获取该ClassLoader下的cacheManager容器
        Map<URI, CacheManager> cacheManagersByURI = cacheManagersByClassLoader.get(managerClassLoader);

        if (Objects.isNull(cacheManagersByURI)) {
            // 如果ClassLoader下目前没有相应的CacheManger，则创建一个容器
            cacheManagersByURI = new ConcurrentHashMap<>();
        }

        //通过uri，获取该ClassLoader下管理的CacheManager
        CacheManager cacheManager = cacheManagersByURI.get(managerURI);

        if (Objects.isNull(cacheManager)) {
            // 如果当前Provider管理的CacheManager容器中没有该URI对应的Manager，则创建一个
            cacheManager = new JCacheManager(
                    this,
                    managerURI,
                    managerClassLoader,
                    managerProperties);
            //然后将新创建的CacheManger放到CacheProvider的CacheManager容器中
            cacheManagersByURI.put(managerURI, cacheManager);
        }
        if (!cacheManagersByClassLoader.containsKey(managerClassLoader)) {
            //如果该Provider不包含目前ClassLoader下的cacheManager容器，则将其加入其中
            cacheManagersByClassLoader.put(managerClassLoader, cacheManagersByURI);
        }
        return cacheManager;
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public URI getDefaultURI() {
        try {
            return new URI(this.getClass().getName());
        } catch (URISyntaxException e) {
            throw new CacheException("JCachingProvider获取默认URI失败", e);
        }
    }

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, getDefaultProperties());
    }

    @Override
    public CacheManager getCacheManager() {
        return getCacheManager(getDefaultURI(), getDefaultClassLoader(), getDefaultProperties());
    }

    /**
     * 关闭该provider下所有classLoader的cacheManager
     */
    @Override
    public synchronized void close() {
        // 使用一个新的引用接受原容器
        Map<ClassLoader, Map<URI, CacheManager>> managersByClassLoader = this.cacheManagersByClassLoader;
        // 给原容器赋值一个新的空容器
        this.cacheManagersByClassLoader = new WeakHashMap<>();
        // 将原容器总所有的cacheManager清空
        for (ClassLoader classLoader : managersByClassLoader.keySet()) {
            for (CacheManager cacheManager : managersByClassLoader.get(classLoader).values()) {
                logger.debug("classLoader：{},uri:{}", classLoader, cacheManager.getURI());
                cacheManager.close();
            }
        }
    }

    /**
     * 关闭指定classLoader下的所有cacheManger
     * 如果classLoader为空，则关闭默认ClassLoader下的所有CacheManager
     */
    @Override
    public synchronized void close(ClassLoader classLoader) {
        ClassLoader managerClassLoader = Objects.isNull(classLoader) ? getDefaultClassLoader() : classLoader;
        Map<URI, CacheManager> uriCacheManagerMap = cacheManagersByClassLoader.get(managerClassLoader);
        if (Objects.nonNull(uriCacheManagerMap)) {
            for (CacheManager cacheManager : uriCacheManagerMap.values()) {
                logger.debug("classLoader：{},uri:{}", classLoader, cacheManager.getURI());
                cacheManager.close();
            }
        }
    }

    /**
     * 关闭指定classLoader下指定uri的cacheManager
     * 如果classloader为空，uri为空，则均关闭默认classLoader下默认uri的cacheManger
     */
    @Override
    public synchronized void close(URI uri, ClassLoader classLoader) {
        URI managerURI = Objects.isNull(uri) ? getDefaultURI() : uri;
        ClassLoader managerClassLoader = Objects.isNull(classLoader) ? getDefaultClassLoader() : classLoader;
        Map<URI, CacheManager> cacheManagersByURI = cacheManagersByClassLoader.get(managerClassLoader);
        if (Objects.nonNull(cacheManagersByURI)) {
            CacheManager cacheManager = cacheManagersByURI.remove(managerURI);
            if (Objects.nonNull(cacheManager)) {
                logger.debug("classLoader：{},uri:{}", classLoader, cacheManager.getURI());
                cacheManager.close();
            }
            if (cacheManagersByURI.size() == 0) {
                // 如果此时该classLoader下已无CacheManager,则将该ClassLoader管理的CacheManager容器从Provider中移除
                cacheManagersByClassLoader.remove(managerClassLoader);
            }
        }
    }

    /**
     * 判断是否支持引用存储
     */
    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        if (optionalFeature == OptionalFeature.STORE_BY_REFERENCE) {
            logger.debug(">>>>>>>JCachingProvider支持引用方式存储>>>>>>");
            return true;
        }
        logger.debug(">>>>>>>JCachingProvider不支持引用方式存储>>>>>>");
        return false;
    }

    public synchronized void releaseCacheManager(URI uri, ClassLoader classLoader) {
        URI managerURI = uri == null ? getDefaultURI() : uri;
        ClassLoader managerClassLoader = classLoader == null ? getDefaultClassLoader() : classLoader;
        Map<URI, CacheManager> cacheManagersByURI = cacheManagersByClassLoader.get(managerClassLoader);
        if (cacheManagersByURI != null) {
            cacheManagersByURI.remove(managerURI);
            if (cacheManagersByURI.size() == 0) {
                cacheManagersByClassLoader.remove(managerClassLoader);
            }
        }
    }
}
