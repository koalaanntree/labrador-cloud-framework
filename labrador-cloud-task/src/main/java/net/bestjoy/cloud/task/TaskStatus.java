package net.bestjoy.cloud.task;

/***
 * 任务常量
 *
 */
public final class TaskStatus {
    /**
     * 新任务，初始状态
     */
    final static public int NEW = 0;

    /**
     * 执行中
     */
    final static public int PROCESSING = 1;


    /**
     * 执行成功
     */
    final static public int DONE = 2;

    /**
     * 执行失败
     */
    final static public int FAIL = 3;


    /**
     * 无效任务，任务关联的业务实体不存在
     */
    final static public int INVALID = 4;
}
