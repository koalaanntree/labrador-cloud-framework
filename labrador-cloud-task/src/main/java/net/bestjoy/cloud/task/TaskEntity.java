package net.bestjoy.cloud.task;

import java.util.Date;

/**
 * 任务实体
 *
 */
public interface TaskEntity<K, T> {
    /**
     * 任务id，主键
     * @return 任务唯一id
     */
    K      getTaskId();

    /**
     * 任务加载批次号
     * @return 批次号
     */
    T       getTaskBatchId();

    /**
     * 任务状态修改时间
     * @return 状态时间
     */
    Date    getTaskModifiedTime();

    /**
     * 任务执行失败累计次数
     * @return 累计失败次数
     */
    Integer getTaskFailedCount();

    /**
     * 任务所属业务域
     * @return 任务域
     */
    String  getBizDomain();


    /**
     * 任务创建时间
     * @return 创建时间
     */
    Date getTaskCreateTime();

    /**
     * 任务执行状态
     * @return 任务执行状态
     */
    int getTaskStatus();
}
