package net.bestjoy.cloud.jdbc.orm;

import net.bestjoy.cloud.jdbc.core.Constants;
import net.bestjoy.cloud.jdbc.core.KeyGenerator;
import net.bestjoy.cloud.jdbc.core.ReflectionUtils;
import net.bestjoy.cloud.jdbc.util.ConvertStringUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SqlBuilder {

    /**
     * 构建insert语句
     *
     * @param entity 实体映射对象
     * @return tableName 自定义表名
     */
    public static BoundSql buildInsertSql(Object entity, String tableName) {
        return buildInsertSql(entity, tableName, null);
    }

    /**
     * 构建insert语句
     *
     * @param entity 实体映射对象
     * @return tableName 自定义表名
     */
    public static BoundSql buildInsertSql(Object entity, String tableName, String pkName) {
        if (StringUtils.isEmpty(pkName)) {
            pkName = Constants.PKID;
        }
        String pkValue = null;
        BoundSql boundSql = new BoundSql();
        //获取对象所有属性和值
        List<String> fieldNames = new ArrayList<String>();
        List<Object> fieldValues = new ArrayList<Object>();
        List<Object> params = new ArrayList<Object>();
        Class<?> entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = ReflectionUtils.getFieldValue(entity, fieldName);
            if (StringUtils.equalsIgnoreCase(pkName, fieldName)) {
                fieldValue = KeyGenerator.defualtKeyGenerator();
                pkValue = (String) fieldValue;
            } else if (fieldValue == null) {
                continue;
            }
            fieldNames.add(fieldName);
            fieldValues.add(fieldValue);
            params.add(fieldValue);
        }
        //获取表名
        if (StringUtils.isEmpty(tableName)) {
            // tableName = getTableName(entityClass);
            return null;
        }
        //组装SQL
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(tableName);
        sql.append("(");
        sql.append(ConvertStringUtil.convertToString(fieldNames, ","));
        sql.append(")");
        sql.append(" values ");
        sql.append("(");
        sql.append(ConvertStringUtil.convertToCustomString(fieldValues, "?", ","));
        sql.append(")");

        boundSql.setParams(params);
        boundSql.setSql(sql.toString());
        boundSql.setPrimaryKeyValue(pkValue);

        return boundSql;
    }

    /**
     * 构建更新sql
     *
     * @param entity
     * @param tableName
     * @param pkName
     * @return
     */
    public static BoundSql buildUpdateSql(Object entity, String tableName, String pkName) {
        BoundSql boundSql = new BoundSql();
        if (StringUtils.isEmpty(tableName)) {
            //tableName = getTableName(entity.getClass());
            return null;
        }
        if (StringUtils.isEmpty(pkName)) {
            pkName = getPKName(entity.getClass());
        }
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableName).append(" set ");

        List<Object> params = new ArrayList<Object>();
        Class<? extends Object> entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = ReflectionUtils.getFieldValue(entity, fieldName);
            if (pkName.equalsIgnoreCase(fieldName) || fieldValue == null) {
                continue;
            }
            sql.append(fieldName + " = ?,");
            params.add(fieldValue);
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ").append(pkName).append(" = ?");

        String pkValue = (String) ReflectionUtils.getFieldValue(entity, pkName);
        params.add(pkValue);

        boundSql.setParams(params);
        boundSql.setPrimaryKey(pkName);
        boundSql.setPrimaryKeyValue(pkValue);
        boundSql.setSql(sql.toString());

        return boundSql;
    }

    /**
     * 构建删除sql
     *
     * @param entity
     * @param tableName
     * @param pkName
     * @return
     */
    public static BoundSql buildDeleteSql(Object entity, String tableName, String pkName) {
        BoundSql boundSql = new BoundSql();
        if (StringUtils.isEmpty(tableName)) {
            //tableName = getTableName(entity.getClass());
            return null;
        }
        if (StringUtils.isEmpty(pkName)) {
            pkName = getPKName(entity.getClass());
        }
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(pkName);
        sql.append(" = ? ");

        String fieldValue = (String) ReflectionUtils.getFieldValue(entity, pkName);
        List<Object> params = new ArrayList<Object>();
        params.add(fieldValue);

        boundSql.setParams(params);
        boundSql.setPrimaryKey(pkName);
        boundSql.setPrimaryKeyValue(fieldValue);
        boundSql.setSql(sql.toString());

        return boundSql;
    }

    /**
     * 构建删除sql
     *
     * @param clazz
     * @param tableName
     * @param pkName
     * @param pkValue
     * @return
     */
    public static BoundSql buildDeleteSql(Class<?> clazz, String tableName, String pkName, String pkValue) {
        BoundSql boundSql = new BoundSql();
        if (StringUtils.isEmpty(tableName)) {
            // tableName = getTableName(clazz);
            return null;
        }
        if (StringUtils.isEmpty(pkName)) {
            pkName = getPKName(clazz);
        }
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(pkName);
        sql.append(" = ? ");

        List<Object> params = new ArrayList<Object>();
        params.add(pkValue);

        boundSql.setParams(params);
        boundSql.setPrimaryKey(pkName);
        boundSql.setSql(sql.toString());

        return boundSql;
    }

    /**
     * 构建查询 的 sql
     *
     * @param clazz
     * @param tableName
     * @param pkName
     * @param pkValue
     * @return
     */
    public static BoundSql buildQuerySql(Class<?> clazz, String tableName, String pkName, String pkValue) {
        BoundSql boundSql = new BoundSql();
        if (StringUtils.isEmpty(tableName)) {
            //tableName = getTableName(clazz);
            return null;
        }
        if (StringUtils.isEmpty(pkName)) {
            pkName = getPKName(clazz);
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select * from ");
        sql.append(tableName);
        sql.append(" where 1=1 ");

        List<Object> params = null;
        if (pkValue != null) {
            sql.append(" and ");
            sql.append(pkName);
            sql.append(" = ? ");

            params = new ArrayList<Object>();
            params.add(pkValue);
        }

        boundSql.setParams(params);
        boundSql.setPrimaryKey(pkName);
        boundSql.setSql(sql.toString());
        boundSql.setPrimaryKeyValue(pkValue);

        return boundSql;
    }

    public static String getPKName(Class<?> entityClass) {
        return Constants.PKID;
    }
}
