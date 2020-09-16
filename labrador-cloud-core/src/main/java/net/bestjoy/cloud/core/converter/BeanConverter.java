package net.bestjoy.cloud.core.converter;

/***
 * 装换bean
 *
 * @param <Origin> 原始数据
 * @param <Target> 转换后数据
 * @author ray
 */
@FunctionalInterface
public interface BeanConverter<Target, Origin> {
    /**
     * 转换对象
     *
     * @param origin 原始数据
     * @return 转换结果
     */
    Target convert(Origin origin);
}
