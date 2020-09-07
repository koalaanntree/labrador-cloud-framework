package net.bestjoy.cloud.task.registry.internal;

import net.bestjoy.cloud.task.registry.spi.DaoSelector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DaoSelectorImpl implements DaoSelector, ApplicationContextAware {

    /**
     * 获取到对应的dao
     *
     * @param strategy
     * @param dialectName
     * @return
     */
    @Override
    public <T> T resolveStrategy(Class<T> strategy, String dialectName) {
        if (StringUtils.isEmpty(dialectName)) {
            dialectName = "MySql";
        }
        /**
         * 拼接dialectName对应的dao实例完整路径
         */
        String implName = strategy.getPackage().getName() + "." + dialectName + strategy.getSimpleName();

        try {
            return appCtx.getBean((Class<T>) Class.forName(implName));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    private static ApplicationContext appCtx;

    /**
     * 重写setApplicationContext方法
     * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
     *
     * @param applicationContext ApplicationContext 对象.
     * @throws BeansException
     * @author guanzheng
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }
}