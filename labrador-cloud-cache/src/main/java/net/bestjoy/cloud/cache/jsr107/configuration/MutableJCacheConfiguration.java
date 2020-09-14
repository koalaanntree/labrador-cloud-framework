package net.bestjoy.cloud.cache.jsr107.configuration;

import net.bestjoy.cloud.cache.jsr107.codec.DefaultJCacheCodecFactory;
import net.bestjoy.cloud.cache.jsr107.codec.JCacheCodecFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 */
public class MutableJCacheConfiguration<K, V> implements JCacheConfiguration<K, V> {
    // 定义缓存名称
    private String name;
    // 定义缓存key值类型
    private Class<K> keyType;
    // 定义缓存value值类型
    private Class<V> valueType;
    // 定义缓存keyType的规范名称
    private String[] keyTypeCanonicalName = {};
    // 定义缓存value的规范名称
    private String valueTypeCanonicalName = "";
    // 定义编码Factory
    private Class<? extends JCacheCodecFactory> cacheCodecFactory = DefaultJCacheCodecFactory.class;
    // 定义是否可以被统计，默认为true
    private boolean statisticsEnabled = Boolean.TRUE;
    // 定义是否可以被管理，默认为false
    private boolean managementEnabled = Boolean.FALSE;
    // 定义默认过期时间 - 7天
    private long expiryForUpdate = 7 * 24 * 60 * 60;
    // 定义时间duration单位，默认为秒
    private TimeUnit expiryForUpdateTimeUnit = TimeUnit.SECONDS;
    // 定义缓存是否开启redis，默认开启
    private boolean redisCacheEnabled = Boolean.TRUE;
    // 定义缓存是否开启内存缓存，默认开启
    private boolean inMemoryCacheEnabled = Boolean.TRUE;
    // 定义内存缓存最大存储条目数量 ，默认1024条
    private int inMemoryCacheMaxEntry = 1024;
    // 定义内存缓存过期时间，默认24小时
    private long inMemoryCacheExpiryForUpdate = 24 * 60 * 60;
    // 定义内存缓存过期时间单位，默认为秒
    private TimeUnit inMemoryCacheExpiryForUpdateTimeUnit = TimeUnit.SECONDS;

    // 额外参数
    private List<String> exConfigurations = new ArrayList<>();

    public MutableJCacheConfiguration() {
    }

    public MutableJCacheConfiguration(JCacheConfiguration<K, V> configuration) {
        this.name = configuration.getName();
        this.keyType = configuration.getKeyType();
        this.valueType = configuration.getValueType();
        this.keyTypeCanonicalName = configuration.getKeyTypeCanonicalName();
        this.valueTypeCanonicalName = configuration.getValueTypeCanonicalName();
        this.cacheCodecFactory = configuration.getJCacheCodecFactory();
        this.statisticsEnabled = configuration.isStatisticsEnabled();
        this.managementEnabled = configuration.isManagementEnabled();
        this.expiryForUpdate = configuration.getExpiryForUpdate();
        this.expiryForUpdateTimeUnit = configuration.getExpiryForUpdateTimeUnit();
        this.redisCacheEnabled = configuration.isRedisCacheEnabled();
        this.inMemoryCacheEnabled = configuration.isInMemoryCacheEnabled();
        this.inMemoryCacheMaxEntry = configuration.getInMemoryCacheMaxEntry();
        this.inMemoryCacheExpiryForUpdate = configuration.getInMemoryCacheExpiryForUpdate();
        this.inMemoryCacheExpiryForUpdateTimeUnit = configuration.getInMemoryCacheExpiryForUpdateTimeUnit();
        this.exConfigurations = configuration.getExConfigurations();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Class<K> getKeyType() {
        return keyType;
    }

    public void setKeyType(Class<K> keyType) {
        this.keyType = keyType;
    }

    @Override
    public Class<V> getValueType() {
        return valueType;
    }

    public void setValueType(Class<V> valueType) {
        this.valueType = valueType;
    }

    @Override
    public String[] getKeyTypeCanonicalName() {
        return keyTypeCanonicalName;
    }

    public void setKeyTypeCanonicalName(String[] keyTypeCanonicalName) {
        this.keyTypeCanonicalName = keyTypeCanonicalName;
    }

    @Override
    public String getValueTypeCanonicalName() {
        return valueTypeCanonicalName;
    }

    public void setValueTypeCanonicalName(String valueTypeCanonicalName) {
        this.valueTypeCanonicalName = valueTypeCanonicalName;
    }

    @Override
    public Class<? extends JCacheCodecFactory> getJCacheCodecFactory() {
        return cacheCodecFactory;
    }

    public void setCacheCodecFactory(Class<? extends JCacheCodecFactory> cacheCodecFactory) {
        this.cacheCodecFactory = cacheCodecFactory;
    }

    @Override
    public long getExpiryForUpdate() {
        return expiryForUpdate;
    }

    public void setExpiryForUpdate(long expiryForUpdate) {
        this.expiryForUpdate = expiryForUpdate;
    }


    @Override
    public TimeUnit getExpiryForUpdateTimeUnit() {
        return expiryForUpdateTimeUnit;
    }

    public void setExpiryForUpdateTimeUnit(TimeUnit expiryForUpdateTimeUnit) {
        this.expiryForUpdateTimeUnit = expiryForUpdateTimeUnit;
    }

    @Override
    public boolean isRedisCacheEnabled() {
        return redisCacheEnabled;
    }

    public void setRedisCacheEnabled(boolean redisCacheEnabled) {
        this.redisCacheEnabled = redisCacheEnabled;
    }

    @Override
    public boolean isInMemoryCacheEnabled() {
        return inMemoryCacheEnabled;
    }

    public void setInMemoryCacheEnabled(boolean inMemoryCacheEnabled) {
        this.inMemoryCacheEnabled = inMemoryCacheEnabled;
    }

    @Override
    public int getInMemoryCacheMaxEntry() {
        return inMemoryCacheMaxEntry;
    }

    public void setInMemoryCacheMaxEntry(int inMemoryCacheMaxEntry) {
        this.inMemoryCacheMaxEntry = inMemoryCacheMaxEntry;
    }

    @Override
    public long getInMemoryCacheExpiryForUpdate() {
        return inMemoryCacheExpiryForUpdate;
    }

    public void setInMemoryCacheExpiryForUpdate(long inMemoryCacheExpiryForUpdate) {
        this.inMemoryCacheExpiryForUpdate = inMemoryCacheExpiryForUpdate;
    }

    @Override
    public TimeUnit getInMemoryCacheExpiryForUpdateTimeUnit() {
        return inMemoryCacheExpiryForUpdateTimeUnit;
    }

    public void setInMemoryCacheExpiryForUpdateTimeUnit(TimeUnit inMemoryCacheExpiryForUpdateTimeUnit) {
        this.inMemoryCacheExpiryForUpdateTimeUnit = inMemoryCacheExpiryForUpdateTimeUnit;
    }

    @Override
    public List<String> getExConfigurations() {
        return exConfigurations;
    }

    public void setExConfigurations(List<String> exConfigurations) {
        this.exConfigurations = exConfigurations;
    }

    @Override
    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

    public void setStatisticsEnabled(boolean statisticsEnabled) {
        this.statisticsEnabled = statisticsEnabled;
    }

    @Override
    public boolean isManagementEnabled() {
        return managementEnabled;
    }

    public void setManagementEnabled(boolean managementEnabled) {
        this.managementEnabled = managementEnabled;
    }
}
