package net.bestjoy.cloud.task.registry.spi;

/**
 *
 */
public interface DaoSelector {

    /**
     * 获取到对应的dao
     *
     * @param strategy
     * @param dialectName
     * @param <T>
     * @return
     */
    <T> T resolveStrategy(Class<T> strategy, String dialectName);

}