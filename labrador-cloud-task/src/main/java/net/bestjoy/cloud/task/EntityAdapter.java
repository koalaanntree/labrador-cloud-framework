package net.bestjoy.cloud.task;

/**
 * 业务实体数据适配器
 * <p>
 * 管理业务实体与任务实体的关系，关联任务与业务实体
 */
public interface EntityAdapter<K, T extends TaskEntity<?, ?>, E> {
    /**
     * 获取任务实体对于的业务实体
     * <p> 一般在任务呗执行时去获取对于的业务实体</p>
     * <p> 常用实现是通过任务的domain/bizId从DB中加载响应的业务实体</p>
     * <p> 如果返回值为null,任务会被标记为TaskStatus.INVALID,任务不在执行</p>
     *
     * @param task 任务实体详情
     * @return 业务实体
     */
    E getEntity(T task);

    /**
     * 获取业务实体主键, 实现任务、业务实体的关联
     *
     * @param entity 业务实体
     * @return 业务实体ID
     */
    K extractEntityId(E entity);
}
