package net.bestjoy.cloud.task.queue.impl;

import net.bestjoy.cloud.task.TaskEntity;
import net.bestjoy.cloud.task.queue.TaskQueue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 默认任务队列实现
 * <p>
 * 使用并发队列管理数据
 */
public class DefaultTaskQueueImpl<K, T extends TaskEntity<K, ?>> implements TaskQueue<K, T> {
    private Queue<T> queue = new ConcurrentLinkedDeque<>();
    private Set<K> keys = new HashSet<>();

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int size() {
        return queue.size();
    }

    /**
     * 添加任务，队列根据taskId去重
     *
     * @param tasks
     */
    @Override
    public int add(Collection<? extends T> tasks) {
        synchronized (this) {
            int n = 0;
            for (T task : tasks) {
                if (!keys.contains(task.getTaskId())) {
                    keys.add(task.getTaskId());
                    queue.add(task);
                    n += 1;
                }
            }

            return n;
        }
    }

    /**
     * 获取一个任务
     *
     * @return 任务
     */
    @Override
    public T pop() {
        synchronized (this) {
            T task = queue.poll();

            if (task != null) {
                keys.remove(task.getTaskId());
            }

            return task;
        }
    }
}
