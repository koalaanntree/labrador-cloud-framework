package net.bestjoy.cloud.jdbc.orm;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;


public class BoundSql {

    /**
     * 执行的sql
     */
    private String sql;

    /**
     * 参数，对应sql中的?号
     */
    private List<Object> params;

    /**
     * 主键名称
     */
    private String primaryKey = "id";

    /**
     * 主键值
     */
    private String primaryKeyValue;

    public BoundSql() {

    }

    public BoundSql(String sql, String primaryKey, List<Object> params) {
        this.sql = sql;
        this.primaryKey = primaryKey;
        this.params = params;
    }

    public BoundSql(String sql, List<Object> params, String primaryKey, String primaryKeyValue) {
        this.sql = sql;
        this.params = params;
        this.primaryKey = primaryKey;
        this.primaryKeyValue = primaryKeyValue;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKeyValue() {
        return primaryKeyValue;
    }

    public void setPrimaryKeyValue(String primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }

    @Override
    public String toString() {
        return "BoundSql [sql=" + sql + ", params=" + convertToString(params, ",") + ", primaryKey=" + primaryKey
                + ", primaryKeyValue=" + primaryKeyValue + "]";
    }


    private String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

}
