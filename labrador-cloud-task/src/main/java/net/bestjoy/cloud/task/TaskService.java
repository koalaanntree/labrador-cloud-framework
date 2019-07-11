package net.bestjoy.cloud.task;

/**
 * 任务系统核心类
 *
 * <p>任务服务的生命周期和任务的创建</p>
 * <p>常用模式：</p>
 * <p>  在业务实体存储完成后，调用addTaskByBizEntity/addTaskByBizEntityId向任务系统添加任务。</p>
 * <p>任务和业务实体通过业务实体主键ID和业务域关联, 在任务执行时EntityAdapter传入任务主键获取业务实体，在使用任务实体+业务实体调用任务处理器的业务逻辑</p>
 * <p>使用者核心关注点是实现业务相关的： 业务实体(BizEntity)、业务实体适配器(EntityAdapter)、任务逻辑处理器(TaskHandler)</p>
 */
public interface TaskService<K, E> {

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 执行loop的一步，加载任务并通知worker消费
     */
    void oneStep();

    /**
     * 通过业务实体添加任务
     *
     * @param entity 业务实体
     * @param domain 业务域
     */
    void addTaskByBizEntity(E entity, String domain);

    /**
     * 通过业务实体主键ID添加任务
     *
     * @param bizId  业务主键
     * @param domain 业务域
     */
    void addTaskByBizEntityId(K bizId, String domain);
}
