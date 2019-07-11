package net.bestjoy.cloud.task.standalone.dao;

import net.bestjoy.cloud.task.TaskStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 */
@Mapper
public interface OracleTaskDAO extends TaskDAO {


    @UpdateProvider(type = TaskSqlProvider.class, method = "batchMarkFreeTasksWithDomainOracle")
    @Override
    int batchMarkFreeTasksWithDomain(@Param("tableName") String tableName,
                                     @Param("batchId") String batchId,
                                     @Param("bizDomain") String bizDomain,
                                     @Param("n") int n,
                                     @Param("timeout") long timeout,
                                     @Param("failReloadTime") long failReloadTime,
                                     @Param("maxFailedCount") int maxFailedCount,
                                     @Param("delay") long delay);

    @Select("SELECT * FROM ${tableName} WHERE batch_id=#{batchId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "batch_id", property = "batchId"),
            @Result(column = "biz_id", property = "bizId"),
            @Result(column = "biz_domain", property = "bizDomain"),
            @Result(column = "failed_count", property = "failedCount"),
            @Result(column = "status", property = "status"),
            @Result(column = "gmt_modified", property = "gmtModified"),
            @Result(column = "gmt_create", property = "gmtCreate"),
    })
    @Override
    List<TaskDO> selectByBatchId(@Param("tableName") String tableName,
                                 @Param("batchId") String batchId);


    @Update("UPDATE ${tableName} SET status=#{status}, failed_count=failed_count + #{failedCountInc} WHERE id=#{taskId}")
    @Override
    int updateTaskStatus(@Param("tableName") String tableName,
                         @Param("taskId") long taskId,
                         @Param("status") int status,
                         @Param("failedCountInc") int failedCountInc);

    @Insert("INSERT INTO ${tableName} (biz_domain, biz_id, failed_count, gmt_modified, gmt_create, status)" +
            "values (#{taskDO.bizDomain}, #{taskDO.bizId}, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, " + TaskStatus.NEW + ")")
    @SelectKey(statement = "SELECT task_queue_id_seq.currval from dual", keyProperty = "id", resultType = Long.class, before = false)
    @Override
    int insert(@Param("tableName") String tableName, @Param("taskDO") TaskDO taskDO);
}
