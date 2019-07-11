/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package net.bestjoy.cloud.task.standalone.dao;

import net.bestjoy.cloud.task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 */
public class TaskSqlProvider<T> {

    private static final Logger logger = LoggerFactory.getLogger(TaskSqlProvider.class);

    public String batchMarkFreeTasksWithDomainOracle(Map<String, Object> param) {

        String table = (String) param.get("tableName");
        String batchId = (String) param.get("batchId");
        String bizDomain = (String) param.get("bizDomain");
        int n = (Integer) param.get("n");
        long timeout = (Long) param.get("timeout");
        long failReloadTime = (Long) param.get("failReloadTime");
        int maxFailedCount = (Integer) param.get("maxFailedCount");
        long delay = (Long) param.get("delay");

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append("" + table + "").append(
                " SET  batch_id = '" + batchId + "',gmt_modified = CURRENT_TIMESTAMP, status = "
                        + TaskStatus.PROCESSING +
                        " WHERE ");

        sql.append("(status=" + TaskStatus.NEW);

        //失败并且失败重试时间超限
        sql.append(" OR (status=" + TaskStatus.FAIL);
        if (failReloadTime > 0) {
            sql.append(" AND ROUND(TO_NUMBER(sysdate - gmt_modified)* 24 * 60 * 60) >=  "
                    + failReloadTime);
        }
        sql.append(")");

        //抢占处理超时的任务
        sql.append(" OR (status=" + TaskStatus.PROCESSING
                + " AND ROUND(TO_NUMBER(sysdate - gmt_modified)* 24 * 60 * 60) > " + timeout + ")");
        sql.append(")");

        // 延时加载
        if (delay > 0) {
            sql.append(" AND ROUND(TO_NUMBER(sysdate - gmt_create) * 24 * 60 * 60) >= " + delay);
        }
        if (maxFailedCount > 0) {
            sql.append(" AND failed_count < " + maxFailedCount);
        }

        if (bizDomain != null && bizDomain.trim().length() > 0) {
            sql.append(" AND biz_domain='" + bizDomain + "'");
        }

        //限制条数
        sql.append(" AND ROWNUM <= " + n);

        logger.debug("updateFreeTasksWithDomain: " + sql.toString());
        return sql.toString();
    }

    public String batchMarkFreeTasksWithDomainMysql(Map<String, Object> param) {

        String table = (String) param.get("tableName");
        String batchId = (String) param.get("batchId");
        String bizDomain = (String) param.get("bizDomain");
        int n = (Integer) param.get("n");
        long timeout = (Long) param.get("timeout");
        long failReloadTime = (Long) param.get("failReloadTime");
        int maxFailedCount = (Integer) param.get("maxFailedCount");
        long delay = (Long) param.get("delay");

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(table).append(
                " SET  batch_id = '" + batchId + "',gmt_modified = now(), status = "
                        + TaskStatus.PROCESSING +
                        " WHERE ");

        sql.append("(`status`=" + TaskStatus.NEW);

        //失败并且失败重试时间超限
        sql.append(" OR (`status`=" + TaskStatus.FAIL);
        if (failReloadTime > 0) {
            sql.append(" AND TIMESTAMPDIFF(SECOND, gmt_modified, now()) >= "
                    + failReloadTime);
        }
        sql.append(")");

        //抢占处理超时的任务
        sql.append(" OR (`status`=" + TaskStatus.PROCESSING
                + " AND TIMESTAMPDIFF(SECOND, gmt_modified, now()) > " + timeout + ")");
        sql.append(")");

        // 延时加载
        if (delay > 0) {
            sql.append(" AND TIMESTAMPDIFF(SECOND, gmt_create, now()) >=  " + delay);
        }
        if (maxFailedCount > 0) {
            sql.append(" AND failed_count < " + maxFailedCount);
        }

        if (bizDomain != null && bizDomain.trim().length() > 0) {
            sql.append(" AND biz_domain='" + bizDomain + "'");
        }

        //限制条数
        sql.append(" LIMIT " + n);

        logger.debug("updateFreeTasksWithDomain: " + sql.toString());
        return sql.toString();
    }
}