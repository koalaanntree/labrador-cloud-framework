package net.bestjoy.cloud.task.standalone;

import net.bestjoy.cloud.task.TaskHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 独立任务模式任务处理聚合管理类
 *
 * <p>根据实体class查找对应的任务处理器，如果不存在就使用默认的业务处理器</p>
 */
public class StandaloneTaskHandler implements TaskHandler<BasicTaskEntity, Object> {
    private Map<Class<?>, TaskHandler<BasicTaskEntity, Object>> handlers = new ConcurrentHashMap<>();

    private TaskHandler<BasicTaskEntity, Object> defaultHandler;

    /**
     * 聚合业务处理
     *
     * @param entity 业务实体
     * @param task   任务实体信息
     * @return
     */
    @Override
    public boolean handle(Object entity, BasicTaskEntity task) {
        TaskHandler<BasicTaskEntity, Object> handler = handlers.get(entity.getClass());

        if (handler == null) {
            handler = defaultHandler;
        }

        return handler != null ? handler.handle(entity, task) : false;
    }

    /**
     * 添加业务处理器
     *
     * @param handler 任务处理器
     * @param clazz   业务实体class
     * @param <E>     业务实体类型
     */
    public <E> void add(TaskHandler<BasicTaskEntity, E> handler, Class<? extends E> clazz) {
        handlers.put(clazz, (TaskHandler<BasicTaskEntity, Object>) handler);
    }

    /**
     * 移除业务实体类型对应的任务处理器
     *
     * @param clazz 业务实体类型class
     * @param <E>   业务实体类型
     */
    public <E> void remove(Class<? extends E> clazz) {
        handlers.remove(clazz);
    }

    /**
     * 移除任务处理器
     *
     * @param handler 任务处理器
     * @param <E>     业务实体类型
     */
    public <E> void remove(TaskHandler<BasicTaskEntity, E> handler) {
        Class<?> key = null;

        for (Map.Entry<Class<?>, TaskHandler<BasicTaskEntity, Object>> entry : handlers.entrySet()) {
            if (entry.getValue() == handler) {
                handlers.remove(entry.getKey());
                return;
            }
        }
    }

    /**
     * 获取默认业务处理器
     *
     * @return
     */
    public TaskHandler<BasicTaskEntity, Object> getDefaultHandler() {
        return defaultHandler;
    }

    /**
     * 设定默认业务处理器
     *
     * @param defaultHandler
     */
    public void setDefaultHandler(TaskHandler<BasicTaskEntity, Object> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }
}
