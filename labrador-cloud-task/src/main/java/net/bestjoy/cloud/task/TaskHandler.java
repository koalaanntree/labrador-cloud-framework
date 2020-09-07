package net.bestjoy.cloud.task;

/**
 * 任务处理器，负责具体某项任务的业务逻辑执行，框架会根据任务的执行结果标记更新任务的状态
 */
public interface TaskHandler<T extends TaskEntity<?, ?>, E> {
    /**
     * 业务逻辑处理
     *
     * @param entity 业务实体
     * @param task   任务实体信息
     * @return true: 任务处理完成; false: 任务处理失败，任务会稍后重试; 抛出异常: 等同于处理返回false
     */
    boolean handle(E entity, T task);
}
