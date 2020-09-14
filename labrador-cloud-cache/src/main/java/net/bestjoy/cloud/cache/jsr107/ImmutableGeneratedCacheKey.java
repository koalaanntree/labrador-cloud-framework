package net.bestjoy.cloud.cache.jsr107;

import javax.cache.annotation.GeneratedCacheKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 缓存键（不可变）
 * 键值生成策略
 */
public abstract class ImmutableGeneratedCacheKey implements GeneratedCacheKey {
    /**
     * 定义获取指定位置key值的方法
     */
    public abstract Object getKey(int index);

    /**
     * 获取key的大小，比如：
     * 0： 没有key
     * 1： 一个key
     * 2： 两个key....
     */
    public abstract int size();

    /**
     * 获取key值列表
     */
    public abstract List<Object> getKeys();


    /**
     * 定义 空 键生成策略
     * 该键无值,就是一个空数组
     */
    public static ImmutableGeneratedCacheKey empty() {
        return EmptyGeneratedCacheKey.INSTANCE;
    }

    /**
     * 定义 NULL 键生成策略
     * 该键就只有一个值  null，是单Key生成策略的一个特例
     */
    public static ImmutableGeneratedCacheKey ofNull() {
        return SingletonGeneratedCacheKey.NULL;
    }

    /**
     * 单键生成策略
     * 如果key是null，则走 NULL键生成策略
     * 如果key不为空，则根据key生成单个键
     */
    public static ImmutableGeneratedCacheKey of(Object key) {
        return Objects.isNull(key) ? ofNull() : new SingletonGeneratedCacheKey(key);
    }

    /**
     * 多键值生成策略
     * <p>
     * 如果 keys为空数组，则生成empty
     * 如果 keys只有一个值，则生成单值
     * 如果 keys有多个值，则生成多值key
     */
    public static ImmutableGeneratedCacheKey of(Object... keys) {
        if (keys.length == 0) {
            return empty();
        }
        if (keys.length == 1) {
            return of(keys[0]);
        }
        return new ArrayGeneratedCacheKey(keys);
    }


    private static final class EmptyGeneratedCacheKey extends ImmutableGeneratedCacheKey {
        private static final EmptyGeneratedCacheKey INSTANCE = new EmptyGeneratedCacheKey();

        private EmptyGeneratedCacheKey() {
        }

        public Object getKey(int index) {
            throw new IndexOutOfBoundsException();
        }

        public int size() {
            return 0;
        }

        public List<Object> getKeys() {
            return Collections.emptyList();
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof ImmutableGeneratedCacheKey) {
                ImmutableGeneratedCacheKey that = (ImmutableGeneratedCacheKey) o;
                return that.size() == this.size();
            }
            return false;
        }

        @Override
        public String toString() {
            return "";
        }
    }

    private static final class SingletonGeneratedCacheKey extends ImmutableGeneratedCacheKey {
        private static final SingletonGeneratedCacheKey NULL = new SingletonGeneratedCacheKey(null);
        private final Object key;

        private SingletonGeneratedCacheKey(Object key) {
            this.key = key;
        }

        public Object getKey(int index) {
            if (index == 0) {
                return key;
            }
            throw new IndexOutOfBoundsException();
        }

        public int size() {
            return 1;
        }

        public List<Object> getKeys() {
            return Collections.singletonList(key);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof ImmutableGeneratedCacheKey) {
                ImmutableGeneratedCacheKey that = (ImmutableGeneratedCacheKey) o;
                return that.size() == this.size() && Objects.equals(that.getKey(0), this.getKey(0));
            }
            return false;
        }

        @Override
        public String toString() {
            return Objects.toString(key);
        }
    }

    private static final class ArrayGeneratedCacheKey extends ImmutableGeneratedCacheKey {
        private final Object[] keys;

        private ArrayGeneratedCacheKey(Object[] keys) {
            this.keys = keys;
        }

        @Override
        public Object getKey(int index) {
            return keys[index];
        }

        @Override
        public int size() {
            return keys.length;
        }

        @Override
        public List<Object> getKeys() {
            return Arrays.asList(keys);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(keys);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof ArrayGeneratedCacheKey) {
                ArrayGeneratedCacheKey that = (ArrayGeneratedCacheKey) o;
                return Arrays.equals(that.keys, this.keys);
            }
            if (o instanceof ImmutableGeneratedCacheKey) {
                ImmutableGeneratedCacheKey that = (ImmutableGeneratedCacheKey) o;
                return that.size() == this.size() && Objects.equals(that.getKeys(), this.getKeys());
            }
            return false;
        }

        @Override
        public String toString() {
            return Arrays.toString(keys);
        }
    }
}
