package net.bestjoy.cloud.task.impl;

import net.bestjoy.cloud.task.*;
import net.bestjoy.cloud.task.queue.TaskQueue;
import net.bestjoy.cloud.task.queue.impl.DefaultTaskQueueImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 */
public class TaskServiceImp<K, T extends TaskEntity<K, ?>, E> implements TaskService<K, E> {
    protected static Logger logger = LoggerFactory.getLogger(TaskServiceImp.class);

    private long lastLoadTime = System.currentTimeMillis();

    private TaskAdapter<K, T> taskAdapter;

    private EntityAdapter<K, T, E> entityAdapter;

    private TaskQueue<K, T> queue;

    private TaskConsumerThread<K, T, E> taskConsumerThread;

    private Object loadLockObject = new Object();

    private boolean autoLoading = false;

    private Object queueLockObject = new Object();

    private TaskServiceConfig config;

    public TaskServiceImp(TaskAdapter<K, T> taskAdapter, EntityAdapter<K, T, E> entityAdapter,
                          TaskHandler<T, E> taskHandler, TaskQueue<K, T> queue, TaskServiceConfig taskServiceConfig) {
        this.taskAdapter = taskAdapter;
        this.entityAdapter = entityAdapter;
        this.queue = queue;
        this.config = taskServiceConfig;

        this.taskConsumerThread = new TaskConsumerThread<>("TaskConsumer",
                taskAdapter, entityAdapter, taskHandler,
                queue,
                taskServiceConfig.getWorkerSize(),
                queueLockObject,
                taskServiceConfig.getQueueWaitMillis(),
                loadLockObject);
    }

    public TaskServiceImp(TaskAdapter<K, T> taskAdapter, EntityAdapter<K, T, E> entityAdapter,
                          TaskHandler<T, E> taskHandler, TaskServiceConfig taskServiceConfig) {
        this(taskAdapter, entityAdapter, taskHandler,
                new DefaultTaskQueueImpl<K, T>(), taskServiceConfig);
    }

    @Override
    public void start() {
        logger.info(getConfig().getBizDomain() + " TaskService start");

        taskConsumerThread.start();

        logger.info(getConfig().getBizDomain() + " TaskService take first step");

        startAutoLoad();
    }

    @Override
    public void stop() {
        taskConsumerThread.stopConsume();

        stopAutoLoad();
    }

    /**
     * 尝试加载任务进内存队列中
     */
    public void tryLoad() {
        TaskServiceConfig config = getConfig();

        long now = System.currentTimeMillis();

        TaskQueue<K, T> queue = getQueue();

        // 这里会遍历queue,高并发的时候会有问题
        int size = queue.size();

        int loadSize = config.getMaxQueueSize() - size;

        //新增任务时,会限制最小加载周期不能低于一定时间
        boolean loadDurationOk = (now - lastLoadTime) >= config.getMinLoadDurationMillis();

        if (loadDurationOk && size < config.getLowerQueueSize() && loadSize > 0) {

            lastLoadTime = now;

            //从数据库中加载任务列表
            List<T> tasks = taskAdapter.load(config.getBizDomain(), loadSize, config.getProcessingTimeout(),
                    config.getFailReloadTime(), config.getMaxFailedCount(), config.getLoadDelaySeconds());


            long dbElapsed = System.currentTimeMillis() - now;

            if (dbElapsed > config.getMinLoadDurationMillis()) {
                logger.warn("任务加载耗时: {} ms, 超过配置中的，最小加载时间间隔lowerLoadDuration: {}", dbElapsed,
                        config.getMinLoadDurationMillis());
            }

            int newTasks = queue.add(tasks);

            long elapsed = System.currentTimeMillis() - now;

            if (logger.isInfoEnabled()) {
                logger.info("TaskLoad result try load: {}, loaded: {}, added new: {}, db load time: {}ms, all load time: {}ms", loadSize, tasks.size(), newTasks, dbElapsed, elapsed);
            }

            if (newTasks > 0) {
                synchronized (queueLockObject) {
                    queueLockObject.notifyAll();
                }
            }
        }
    }

    private void startAutoLoad() {
        if (autoLoading) {
            return;
        }

        long duration = getConfig().getAutoLoadDuration();

        final long waitDuration = duration > 0 ? duration : 5000L;

        logger.info("TaskService AutoLoad: start, service for domain: {}", getConfig().getBizDomain());

        autoLoading = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("TaskService AutoLoad: thread start");
                while (autoLoading) {
                    try {
                        tryLoad();

                        synchronized (loadLockObject) {
                            loadLockObject.wait(waitDuration);
                        }
                    } catch (Exception e) {
                        // TODO, log
                        logger.error("tryLoad failed", e);
                    }
                }

                logger.info("TaskService AutoLoad: thread stopped");
            }
        }, "TaskQueueAutoLoad").start();
    }

    private void stopAutoLoad() {
        logger.info(getConfig().getBizDomain() + " TaskService AutoLoad: try to stop");
        autoLoading = false;


        logger.info(getConfig().getBizDomain() + " TaskService AutoLoad: stopped");
    }

    public void oneStep() {
        notifyTryLoad();
    }

    private void notifyTryLoad() {
        synchronized (loadLockObject) {
            loadLockObject.notify();
        }
    }

    @Override
    public void addTaskByBizEntity(E entity, String domain) {
        addTaskByBizEntityId(entityAdapter.extractEntityId(entity), domain);
    }

    @Override
    public void addTaskByBizEntityId(K bizId, String domain) {
        // add task to queue
        taskAdapter.save(bizId, domain);

        notifyTryLoad();
    }

    public TaskAdapter<K, T> getTaskAdapter() {
        return taskAdapter;
    }

    public EntityAdapter<K, T, E> getEntityAdapterAdapter() {
        return entityAdapter;
    }

    public TaskQueue<K, T> getQueue() {
        return queue;
    }

    protected void setQueue(TaskQueue<K, T> queue) {
        this.queue = queue;
    }

    public TaskServiceConfig getConfig() {
        return config;
    }

}
