package net.bestjoy.cloud.cache.jsr107.configuration;

import org.apache.commons.lang3.StringUtils;

import javax.cache.CacheException;
import java.net.URI;
import java.util.Objects;
import java.util.Properties;

/**
 * 作用：
 * 1. 构建JCache-Redis的properties
 * 2. 获取各属性值
 */
public final class JCacheRedisProps {

    public static final String REDIS_PROPS_HOST_NAME = "host";
    public static final String REDIS_PROPS_PORT_NAME = "port";
    public static final String REDIS_PROPS_DATABASE_NAME = "database";
    public static final String REDIS_PROPS_PASSWORD_NAME = "password";

    public static Builder builder() {
        return new Builder();
    }

    public static String getHost(Properties properties) {
        return properties.getProperty(REDIS_PROPS_HOST_NAME);
    }

    public static int getPort(Properties properties) {
        String port = properties.getProperty(REDIS_PROPS_PORT_NAME);
        return StringUtils.isBlank(port) ? 6379 : Integer.parseInt(port);
    }

    public static int getDataBase(Properties properties) {
        String database = properties.getProperty(REDIS_PROPS_DATABASE_NAME);
        return StringUtils.isBlank(database) ? 0 : Integer.parseInt(database);
    }

    public static String getPassword(Properties properties) {
        String password = properties.getProperty(REDIS_PROPS_PASSWORD_NAME);
        return StringUtils.isBlank(password) ? null : password;

    }

    public static class Builder {
        private String host;
        private int port = 6379;
        private int database = 0;
        private String password;


        private Builder() {
        }

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withDatabase(int database) {
            this.database = database;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Properties build() {
            if (StringUtils.isBlank(this.host)) {
                throw new IllegalArgumentException("没有指定redis的host");
            }
            Properties properties = new Properties();
            properties.setProperty(REDIS_PROPS_HOST_NAME, this.host);
            properties.setProperty(REDIS_PROPS_PORT_NAME, String.valueOf(this.port));
            properties.setProperty(REDIS_PROPS_DATABASE_NAME, String.valueOf(this.database));
            if (StringUtils.isNotBlank(this.password)) {
                properties.setProperty(REDIS_PROPS_PASSWORD_NAME, this.password);
            }
            return properties;
        }
    }

    public static URI getLettuceURI(Properties properties) {
        if (Objects.isNull(properties)) {
            throw new CacheException("必须指定缓存配置");
        }
        if (StringUtils.isBlank(JCacheRedisProps.getHost(properties))) {
            throw new CacheException("必须配置Redis的host属性,配置项名称为host");
        }

        return URI.create("redis://" +
                JCacheRedisProps.getHost(properties) + ":" +
                JCacheRedisProps.getPort(properties) + "/" +
                JCacheRedisProps.getDataBase(properties));
    }
}
