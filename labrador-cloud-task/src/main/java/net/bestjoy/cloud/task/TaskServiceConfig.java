package net.bestjoy.cloud.task;

import lombok.Data;

/***
 * task 配置项
 *
 */
@Data
public class TaskServiceConfig {
    /**
     * 数据库方言
     * 默认MySql
     * MySql/Oracle
     */
    private String dialect = "MySql";

    // queue配置
    /**
     * 最小对接加载时间间隔Millis
     */
    private long minLoadDurationMillis = 1 * 1000L;

    /**
     * 队列元素数量下限，低于下限时，队列会尝试加载填充元素到最大值
     */
    private int lowerQueueSize = 50;

    /**
     * 任务队列最大任务数
     */
    private int maxQueueSize = 500;

    /**
     * 等待
     */
    private long queueWaitMillis = 500L;

    /**
     * 工作线程数
     */

    private int workerSize = 4;

    // 任务加载

    private String taskTableName = "task_queue";
    /**
     * 任务执行超时时间
     */
    private int processingTimeout = 5 * 60;

    /**
     * 任务最大执行次数
     */
    private int maxFailedCount = 5;

    /**
     * 任务重试的加载时间
     */
    private long failReloadTime = 5 * 60;

    /**
     * 加载延时创建，任务在创建后延迟加载
     */
    private int loadDelaySeconds = 0;

    /**
     * 自动加载周期
     */
    private long autoLoadDuration = 5 * 1000L;

    /**
     * 领域名,也就是任务的标识
     */
    private String bizDomain = "";
}
