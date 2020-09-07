package net.bestjoy.cloud.task.standalone;


import net.bestjoy.cloud.task.TaskEntity;

import java.util.Date;

/**
 * 基础任务实体
 * <p>任务作为独立实体管理所有业务的任务</p>
 */
public class BasicTaskEntity implements TaskEntity<Long, String> {
    private Long id;

    private String bizId;

    private String bizDomain;

    private String taskBatchId;

    private Date taskModifiedTime;

    private Integer taskFailedCount;

    private Date taskCreateTime;

    private int status;

    /**
     * 任务id
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getTaskId() {
        return id;
    }

    /**
     * 业务实体组件
     *
     * @return
     */
    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    /**
     * 业务所属域
     *
     * @return
     */
    public String getBizDomain() {
        return bizDomain;
    }

    public void setBizDomain(String bizDomain) {
        this.bizDomain = bizDomain;
    }

    /**
     * 任务加载批次id
     *
     * @return
     */
    @Override
    public String getTaskBatchId() {
        return taskBatchId;
    }

    public void setTaskBatchId(String taskBatchId) {
        this.taskBatchId = taskBatchId;
    }

    /**
     * 任务更新时间
     *
     * @return
     */
    @Override
    public Date getTaskModifiedTime() {
        return taskModifiedTime;
    }

    public void setTaskModifiedTime(Date taskModifiedTime) {
        this.taskModifiedTime = taskModifiedTime;
    }

    /**
     * 任务执行失败次数
     *
     * @return
     */
    @Override
    public Integer getTaskFailedCount() {
        return taskFailedCount;
    }

    public void setTaskFailedCount(Integer taskFailedCount) {
        this.taskFailedCount = taskFailedCount;
    }

    /**
     * 任务创建时间
     *
     * @return
     */
    @Override
    public Date getTaskCreateTime() {
        return taskCreateTime;
    }

    public void setTaskCreateTime(Date taskCreateTime) {
        this.taskCreateTime = taskCreateTime;
    }

    /**
     * 任务状态
     *
     * @return
     */
    @Override
    public int getTaskStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BasicTaskEntity{" +
                "id=" + id +
                ", bizId='" + bizId + '\'' +
                ", bizDomain='" + bizDomain + '\'' +
                ", taskBatchId='" + taskBatchId + '\'' +
                ", taskModifiedTime=" + taskModifiedTime +
                ", taskFailedCount=" + taskFailedCount +
                ", taskCreateTime=" + taskCreateTime +
                ", status=" + status +
                '}';
    }
}
