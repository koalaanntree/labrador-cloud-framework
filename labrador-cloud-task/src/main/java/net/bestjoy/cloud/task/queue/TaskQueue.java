package net.bestjoy.cloud.task.queue;


import net.bestjoy.cloud.task.TaskEntity;

import java.util.Collection;

/**
 * 任务队列,负责任务队列的管理
 */
public interface TaskQueue<K, T extends TaskEntity<K, ?>> {

    /**
     * 判定队列是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 队列长队
     *
     * @return
     */
    int size();

    /**
     * 添加任务元素
     *
     * @param tasks
     */
    int add(Collection<? extends T> tasks);

    /**
     * 任务出队
     *
     * @return
     */
    T pop();
}
