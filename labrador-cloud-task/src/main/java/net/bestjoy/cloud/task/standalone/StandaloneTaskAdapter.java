package net.bestjoy.cloud.task.standalone;

import net.bestjoy.cloud.task.TaskAdapter;
import net.bestjoy.cloud.task.standalone.dao.TaskDAO;
import net.bestjoy.cloud.task.standalone.dao.TaskDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 独立任务适配器
 * <p>负责任务的加载，创建，和状态更新</p>
 *
 */
public class StandaloneTaskAdapter<K>
        implements TaskAdapter<K, BasicTaskEntity> {

    protected static Logger logger = LoggerFactory.getLogger(StandaloneTaskAdapter.class);

    /**
     * 存储任务数据的DB表名
     */
    private String tableName;

    private TaskDAO taskDAO;

    public StandaloneTaskAdapter(TaskDAO taskDAO, String tableName) {
        this.tableName = tableName;
        this.taskDAO = taskDAO;
    }

    /**
     * 加载一定数量满足条件的任务
     *
     * @param n              加载任务数量
     * @param timeout        TaskStatus.PROCESSING 状态任务的超时值，处理时长超过timeout的TaskStatus.PROCESSING任务认为是FreeTask任务
     * @param failReloadTime TaskStatus.FAIL 状态任务的重试时间间隔
     * @param maxFailedCount 任务的最多失败次数，任务被处理失败maxFailedCount,任务不在被加载
     * @param delaySeconds   任务延时，任务在创建后多久才能被加载进任务队列，用于实现延时任务
     * @return FreeTask列表
     */
    @Override
    public List<BasicTaskEntity> load(String bizDomain, int n, long timeout, long failReloadTime, int maxFailedCount,
                                      long delaySeconds) {
        String batchId = genBatchId();
        long now = System.currentTimeMillis();

        taskDAO.batchMarkFreeTasksWithDomain(tableName, batchId, bizDomain, n, timeout, failReloadTime,
                maxFailedCount, delaySeconds);
        long update = System.currentTimeMillis();

        logger.info("update batchid time:{}{}", update - now, "ms");
        List<TaskDO> tasks = taskDAO.selectByBatchId(tableName, batchId);
        logger.info("select batchid time:{}{}", System.currentTimeMillis() - update, "ms");
        ArrayList<BasicTaskEntity> list = new ArrayList<>(tasks.size());

        for (TaskDO taskDO : tasks) {
            BasicTaskEntity taskEntity = new BasicTaskEntity();

            taskEntity.setBizDomain(taskDO.getBizDomain());
            taskEntity.setBizId(taskDO.getBizId());
            taskEntity.setId(taskDO.getId());
            taskEntity.setTaskBatchId(taskDO.getBatchId());
            taskEntity.setTaskFailedCount(taskDO.getFailedCount());
            taskEntity.setTaskModifiedTime(taskDO.getGmtModified());
            taskEntity.setTaskCreateTime(taskDO.getGmtCreate());
            taskEntity.setStatus(taskDO.getStatus());

            list.add(taskEntity);
        }

        return list;
    }

    /**
     * 更新任务状态
     *
     * @param task           任务实体
     * @param status         新的任务状态
     * @param failedCountInc 任务失败计数+failedCountInc
     */
    @Override
    public void updateStatus(BasicTaskEntity task, int status, int failedCountInc) {
        taskDAO.updateTaskStatus(tableName, task.getId(), status, failedCountInc);
    }

    /**
     * 创建并保持任务实体
     *
     * @param bizId  业务实体主键ID
     * @param domain 业务域
     */
    @Override
    public void save(K bizId, String domain) {
        TaskDO taskDO = new TaskDO();

        taskDO.setBizDomain(domain);
        taskDO.setBizId(bizId.toString());

        try {
            taskDAO.insert(tableName, taskDO);
        } catch (Exception e) {
            logger.error("task insert error", e);
        }

    }

    /**
     * 创建唯一批次号
     *
     * @return 批次号
     */
    private String genBatchId() {
        return UUID.randomUUID().toString();
    }
}
