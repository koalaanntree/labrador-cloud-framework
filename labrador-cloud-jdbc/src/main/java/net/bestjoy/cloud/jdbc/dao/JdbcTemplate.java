package net.bestjoy.cloud.jdbc.dao;

import net.bestjoy.cloud.jdbc.core.Constants;
import net.bestjoy.cloud.jdbc.core.ReflectionUtils;
import net.bestjoy.cloud.jdbc.orm.BoundSql;
import net.bestjoy.cloud.jdbc.orm.SqlBuilder;
import net.bestjoy.cloud.jdbc.util.ConvertStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * JdbcTemplate
 *
 * @author lidj
 * @date 2018-01-29
 */
public class JdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {

    private static Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    /**
     * 实体对象转换容器
     */
    private final BasicRowProcessor convert = new BasicRowProcessor();

    /**<----------------添加和更改单条记录 start------------------>**/

    /**
     * 更新数据
     *
     * @param <T>
     * @param table 表名
     * @param bean  实体类
     * @return String 更新失败返回null,更新成功则返回主键ID
     */
    public <T> String updateData(String table, Object bean) {
        return updateData(table, bean, null);
    }

    /**
     * 更新数据
     *
     * @param <T>
     * @param table 表名
     * @return
     */
    public <T> String updateData(String table, Object bean, String pkName) {
        if (StringUtils.isEmpty(pkName)) {
            pkName = Constants.PKID;
        }

        //1.更新数据 -- 如果id不为null，则是更新该数据 -- 否则就是插入新数据
        if ((StringUtils.isNotEmpty((String) ReflectionUtils.getFieldValue(bean, pkName)))) {
            BoundSql boundSql = SqlBuilder.buildUpdateSql(bean, table, pkName);
            int update = super.update(boundSql.getSql(), boundSql.getParams().toArray());
            if (update != -1) {
                return boundSql.getPrimaryKeyValue();
            } else {
                return null;
            }
        }

        //2.插入数据
        final BoundSql boundSql = SqlBuilder.buildInsertSql(bean, table, pkName);
        Integer result = super.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(boundSql.getSql());
                int index = 0;
                for (Object param : boundSql.getParams()) {
                    index++;
                    ps.setObject(index, param);
                }
                return ps;
            }
        });

        if (result != -1) {
            return boundSql.getPrimaryKeyValue();
        }

        return null;
    }

    ;

    /**
     * 包含pkid的值保存到数据库（既可更新，也可插入）（宋平2016/05/26）
     *
     * @param table
     * @param bean
     * @return
     */
    public <T> int saveDataWithKey(String table, T bean, String pkName) {
        //获取对象所有属性和值
        if (StringUtils.isEmpty(pkName)) {
            pkName = Constants.PKID;
        }
        if (bean == null) {
            return -1;
        }
        String pkValue = null;
        List<String> fieldNames = new ArrayList<String>();
        List<Object> fieldValues = new ArrayList<Object>();
        final List<Object> params = new ArrayList<Object>();
        Class entityClass = bean.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = ReflectionUtils.getFieldValue(bean, fieldName);
            if (StringUtils.equalsIgnoreCase(pkName, fieldName)) {
                //fieldValue = StringUtils.generateSequenceNo();
                pkValue = (String) fieldValue;
            } else if (fieldValue == null) {
                continue;
            }
            fieldNames.add(fieldName);
            fieldValues.add(fieldValue);
            params.add(fieldValue);
        }
        //获取表名
        if (StringUtils.isEmpty(table)) {
            // table = SqlBuilder.getTableName(entityClass);
            return -1;
        }

        if (!StringUtils.isEmpty(pkValue)) {
            T t = (T) this.queryBeanById(entityClass, table, pkName, pkValue);
            if (t == null) {
                //组装SQL
                final StringBuilder sql = new StringBuilder("insert into ");
                sql.append(table);
                sql.append("(");
                sql.append(ConvertStringUtil.convertToString(fieldNames, ","));
                sql.append(")");
                sql.append(" values ");
                sql.append("(");
                sql.append(ConvertStringUtil.convertToCustomString(fieldValues, "?", ","));
                sql.append(")");

                Integer result = super.update(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(sql.toString());
                        int index = 0;
                        for (Object param : params) {
                            index++;
                            ps.setObject(index, param);
                        }
                        return ps;
                    }
                });
                return result;
            } else {
                BoundSql boundSql = SqlBuilder.buildUpdateSql(bean, table, pkName);
                int update = super.update(boundSql.getSql(), boundSql.getParams().toArray());
                return update;
            }
        } else {
            //2.插入数据
            final BoundSql boundSql = SqlBuilder.buildInsertSql(bean, table, pkName);
            Integer result = super.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(boundSql.getSql());
                    int index = 0;
                    for (Object param : boundSql.getParams()) {
                        index++;
                        ps.setObject(index, param);
                    }
                    return ps;
                }
            });
            return result;
        }
    }
    /**<----------------添加和更改单条记录 end------------------>**/


    /**<----------------通过主键值查询单条记录 start------------------>**/
    /**
     * 通过默认主键值查询单条记录
     *
     * @param beanClass -- 需要查询的实体类
     * @param <T>
     * @return
     */
    public <T> T queryBeanById(final Class<T> beanClass) {
        return this.queryBeanById(beanClass, null);
    }

    /**
     * @param beanClass
     * @param pkValue
     * @param <T>
     * @return
     */
    public <T> T queryBeanById(final Class<T> beanClass, String pkValue) {
        return this.queryBeanById(beanClass, pkValue, null);
    }

    /**
     * @param beanClass
     * @param pkValue
     * @param tableName
     * @param <T>
     * @return
     */
    public <T> T queryBeanById(final Class<T> beanClass, String pkValue, String tableName) {
        return this.queryBeanById(beanClass, tableName, null, pkValue);
    }

    /**
     * @param beanClass
     * @param tableName
     * @param pkName
     * @param pkValue
     * @param <T>
     * @return
     */
    public <T> T queryBeanById(final Class<T> beanClass, String tableName, String pkName, String pkValue) {
        BoundSql buildByIdSql = SqlBuilder.buildQuerySql(beanClass, tableName, pkName, pkValue);
        return query(buildByIdSql.getSql(), buildByIdSql.getParams().toArray(), new ResultSetExtractor<T>() {
            @Override
            public T extractData(ResultSet rs) throws SQLException, DataAccessException {
                return rs.next() ? convert.toBean(rs, beanClass) : null;
            }
        });
    }
    /**<----------------通过主键值查询单条记录 end------------------>**/


    /**<------------------ 查询单条记录 start--------------------->**/
    /**
     * 查询单个记录
     *
     * @param beanClass 需要查询的实体对象
     * @param sql       查询sql
     * @return T 单条记录实体
     */
    public <T> T queryBean(final Class<T> beanClass, String sql) {
        return query(sql, new ResultSetExtractor<T>() {
            @Override
            public T extractData(ResultSet rs) throws SQLException, DataAccessException {
                return rs.next() ? convert.toBean(rs, beanClass) : null;
            }
        });
    }

    /**
     * 查询单个记录
     *
     * @param beanClass 需要查询的实体对象
     * @param sql       查询sql
     * @param args      查询参数
     * @return T 单条记录实体
     */
    public <T> T queryBean(final Class<T> beanClass, String sql, Object... args) {
        return query(sql, args, new ResultSetExtractor<T>() {
            @Override
            public T extractData(ResultSet rs) throws SQLException, DataAccessException {
                return rs.next() ? convert.toBean(rs, beanClass) : null;
            }
        });
    }
    /**<------------------ 查询单条记录 end--------------------->**/


    /**<------------------ 查询记录集合 start--------------------->**/
    /**
     * 查询所有记录-- 这个暂时无用，因为sqlBuilder中暂时没有对默认tableName和pkName的处理
     *
     * @param beanClass
     * @param <T>
     * @return
     */
    public <T> List<T> queryList(final Class<T> beanClass) {
        BoundSql boundSql = SqlBuilder.buildQuerySql(beanClass, null, null, null);
        return this.queryList(beanClass, boundSql.getSql());
    }

    /**
     * 查询记录集合--根据sql
     *
     * @param beanClass 需要查询的实体对象
     * @param sql       查询sql
     * @return List<T> 记录集
     */
    public <T> List<T> queryList(final Class<T> beanClass, String sql) {
        return query(sql, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return convert.toBean(rs, beanClass);
            }
        });
    }

    /**
     * 查询记录集合
     *
     * @param beanClass 需要查询的实体对象
     * @param sql       查询sql
     * @param args      查询参数
     * @return List<T> 记录集
     */
    public <T> List<T> queryList(final Class<T> beanClass, String sql, Object... args) {
        return query(sql, args, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return convert.toBean(rs, beanClass);
            }
        });
    }

    /**<------------------ 查询记录集合 end--------------------->**/


    /**<------------------ 查询记录数量 start--------------------->**/
    /**
     * @param sql
     * @param <T>
     * @return
     */
    public <T> Integer queryCount(String sql) {
        sql = "select count(*) as total from (" + sql + ") temp";
        return query(sql, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                return rs.next() ? rs.getInt("total") : null;
            }
        });
    }

    /**
     * 查询记录数量
     *
     * @param sql  查询sql
     * @param args 查询参数
     * @return List<T> 记录集
     */
    public <T> Integer queryCount(String sql, Object... args) {
        sql = "select count(*) as total from (" + sql + ") temp";
        return query(sql, args, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                return rs.next() ? rs.getInt("total") : null;
            }
        });
    }
    /**<------------------ 查询记录数量 end--------------------->**/

    /**<------------------ 分页查询 start--------------------->**/
    /**
     * 分页查询 (带参数查询,从索引offset开始, 索引limit结束, 参数为 Object... args) -- 暂时只支持oracle分页
     *
     * @param beanClass 需要查询的实体对象
     * @param sql       查询sql
     * @param offset    查询起始索引,为0则查询一页
     * @param limit     查询最大记录数-就是每页数量pageSize
     * @param args      查询参数
     * @return List<T> 结果集
     */
    public <T> List<T> queryByPage(Class<T> beanClass, String sql, int offset, int limit, Object... args) {
        int startRow = offset;
        int endRow = offset + limit;
        if (startRow > endRow) {
            return null;
        }
        sql = "SELECT * FROM (SELECT ROWNUM RN, res.* FROM (" + sql
                + ") res WHERE ROWNUM <=?) WHERE RN >?";
        Object[] params = null;
        //说明是绑定变量的查询
        if (args != null) {
            //加上两个分页的参数
            int n = (args.length) + 2;
            params = new Object[n];
            for (int i = 0; i < args.length; i++) {
                params[i] = args[i];
            }
            params[n - 2] = endRow;
            params[n - 1] = startRow;
        } else {
            params = new Object[2];
            params[0] = endRow;
            params[1] = startRow;
        }
        List<T> list = queryList(beanClass, sql, params);
        return list;

    }
    /**<------------------ 分页查询 end--------------------->**/
}
