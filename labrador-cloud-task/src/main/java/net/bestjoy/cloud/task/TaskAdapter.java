package net.bestjoy.cloud.task;

import java.util.List;

/**
 * 任务数据适配器, 负责任务数据的加载、管理
 */
public interface TaskAdapter<K, T extends TaskEntity<?, ?>> {
    /**
     * 批量加载任务
     *
     * @param n              加载任务数量
     * @param timeout        TaskStatus.PROCESSING 状态任务的超时值，处理时长超过timeout的TaskStatus.PROCESSING任务认为是FreeTask任务
     * @param failReloadTime TaskStatus.FAIL 状态任务的重试时间间隔
     * @param maxFailedCount 任务的最多失败次数，任务被处理失败maxFailedCount,任务不在被加载
     * @param delaySeconds   任务延时，任务在创建后多久才能被加载进任务队列，用于实现延时任务
     * @return 任务列表
     */
    List<T> load(String bizDoamin, int n, long timeout, long failReloadTime, int maxFailedCount, long delaySeconds);

    /**
     * 更新任务状态
     *
     * @param task           任务实体
     * @param status         新的任务状态
     * @param failedCountInc 任务失败计数+failedCountInc
     */
    void updateStatus(T task, int status, int failedCountInc);

    /**
     * 创建并保存任务
     *
     * @param bizId  业务实体主键ID
     * @param domain 业务域
     */
    void save(K bizId, String domain);
}
