package net.bestjoy.cloud.task.standalone.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
public interface TaskDAO {


    int batchMarkFreeTasksWithDomain(@Param("tableName") String tableName,
                                     @Param("batchId") String batchId,
                                     @Param("bizDomain") String bizDomain,
                                     @Param("n") int n,
                                     @Param("timeout") long timeout,
                                     @Param("failReloadTime") long failReloadTime,
                                     @Param("maxFailedCount") int maxFailedCount,
                                     @Param("delay") long delay);

    List<TaskDO> selectByBatchId(String tableName,
                                 String batchId);

    int updateTaskStatus(String tableName,
                         long taskId,
                         int status,
                         int failedCountInc);

    int insert(String tableName, TaskDO taskDO);

    int remove(String tableName, long taskId);
}
