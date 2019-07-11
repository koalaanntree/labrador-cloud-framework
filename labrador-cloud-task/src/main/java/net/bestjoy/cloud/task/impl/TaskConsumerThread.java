package net.bestjoy.cloud.task.impl;

import net.bestjoy.cloud.task.*;
import net.bestjoy.cloud.task.queue.TaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务消费线程, 持续循环消费任务队列，并将任务提交到Executor中
 */
public class TaskConsumerThread<K, T extends TaskEntity<K, ?>, E> extends Thread {

    final static Logger logger = LoggerFactory.getLogger(TaskConsumerThread.class);

    private TaskHandler<T, E> taskHandler;

    private TaskAdapter<K, T> taskAdapter;

    private EntityAdapter<K, T, E> entityAdapter;

    private TaskQueue<K, T> queue;

    private AtomicBoolean running = new AtomicBoolean(false);

    private ExecutorService executorService;

    private final Object lockObject;

    private long lockTimeout;

    final private Object producerLockObj;

    public TaskConsumerThread(String name,
                              TaskAdapter<K, T> taskAdapter,
                              EntityAdapter<K, T, E> entityAdapter,
                              TaskHandler<T, E> taskHandler,
                              TaskQueue<K, T> queue,
                              int workSize,

                              Object lockObject,
                              long lockTimeout,
                              Object producerLockObj) {
        super(name);
        this.taskHandler = taskHandler;
        this.taskAdapter = taskAdapter;
        this.entityAdapter = entityAdapter;
        this.queue = queue;
        this.lockObject = lockObject;
        this.lockTimeout = lockTimeout;

        this.executorService = Executors.newFixedThreadPool(workSize);

        this.producerLockObj = producerLockObj;
    }

    @Override
    public void run() {
        logger.info("TaskConsumer start to run");
        if (running.get()) {
            return;
        }

        running.set(true);

        while (running.get()) {
            try {
                T task = queue.pop();

                if (task != null) {
                    submit(task);
                } else {
                    synchronized (producerLockObj) {
                        producerLockObj.notify();
                    }

                    synchronized (lockObject) {
                        lockObject.wait(lockTimeout);
                    }
                }
            } catch (Exception e) {
                logger.error("TaskConsumer crash ", e);
            }
        }


        logger.info("TaskConsumer stopped");
    }

    private void submit(final T task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int status = TaskStatus.FAIL;

                try {
                    E entity = entityAdapter.getEntity(task);

                    if (entity == null) {
                        logger.info("get a invalid task: {}", task);
                        status = TaskStatus.INVALID;
                    } else {
                        status = taskHandler.handle(entity, task) ? TaskStatus.DONE : TaskStatus.FAIL;
                    }
                } catch (Exception e) {
                    logger.error("handle task crash on task: {}, ", task, e);
                    status = TaskStatus.FAIL;
                }

                int failedCountInc = status == TaskStatus.FAIL ? 1 : 0;
                taskAdapter.updateStatus(task, status, failedCountInc);
            }
        };

        executorService.execute(runnable);
    }

    public void stopConsume() {
        logger.info("TaskConsumer try stop");
        running.set(false);
        executorService.shutdown();
    }
}
