package net.bestjoy.cloud.jdbc.dao;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * BeanProcessor
 * Copy Apache-DBUtils中的该方法
 * 作用:将数据库中的记录转化为对应的Bean对象
 *
 *
 * @author lidj
 * @date 2018-01-28
 */
public class BeanProcessor {


    /**
     * 特殊的数组值，在 mapColumnsToProperties 中使用，表示bean属性中没有与ResultSet中列名相对应的值
     */
    protected static final int PROPERTY_NOT_FOUND = -1;

    /**
     * 该变量的主要作用是保存bean中的基本类型属性与其默认值的对应关系，
     * 当SQL语句get方法返回NULL值的时候就用从该Map中获取相应的默认值
     */
    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();

    /**
     * ResultSet column to bean property name overrides.
     * 该变量的作用是存放数据库字段名与Bean的属性名之间的对应关系。
     * 而Overrides仿佛告诉我们，这个变量是可以被用户“重写”的，也就是说：用户可以自己定义字段名与属性值的关系。
     *
     * 通过BeanProcessor的构造器我们可以看出，
     * 该变量的值允许用户传递进来，
     * 也就是说，用户可以根据自己的需要提供一组对应关系，用来管理数据库字段到bean属性的映射。
     */
    private final Map<String, String> columnToPropertyOverrides;

    /**
     *初始化基本类型的默认值
     */
    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }


    public BeanProcessor() {
        this(new HashMap<String, String>());
    }


    public BeanProcessor(Map<String, String> columnToPropertyOverrides) {
        super();
        if (columnToPropertyOverrides == null) {
            throw new IllegalArgumentException("columnToPropertyOverrides map cannot be null");
        }
        this.columnToPropertyOverrides = columnToPropertyOverrides;
    }


    /**
     * 该方法最能体现该类的作用：toBean----将ResultSet中的一条记录转化成type类型的Bean对象
     * 该方法的实现用到了反射和BeanInfo类来将数据库字段名与JavaBean属性名进行匹配.
     * 属性被匹配到字段名基于以下因素:
     * <br />
     * <ol>
     *     <li>
     *      JavaBean类中存在一个与字段名同名的属性，并且该属性是可写的(即提供一个可访问的set方法).
     *      名称的比较是忽略大小写的.
     *     </li>
     *     <li>
     *      通过ResultSet的get*方法获得的字段的类型可以被转化成类属性的set方法的参数类型.  如果转换失败了
     *     (比如: Bean属性的类型是int 而数据字段的类型为Timestamp) 那么将抛出一个SQLException异常
     *     </li>
     * </ol>
     *
     *<p>
     * 当ResultSet中返回NULL值的时候，JavaBean中的基本数据类型的属性将被赋予它们对应的默认值，
     * 数字类型的将被赋值为0，布尔类型的将被赋值为false,
     * 对象类型的Bean属性将被赋值为null.这与ResultSet的get*方法的行为是一致的
     *</p>
     * @param rs  提供数据的ResultSet
     * @param type 创建Bean对象所需要的Class类
     * @param <T>  需要创建的Bean的类型
     * @return the newly created bean
     * @throws SQLException if a database access error occurs
     */
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {

        PropertyDescriptor[] props = this.propertyDescriptors(type);

        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        return this.createBean(rs, type, props, columnToProperty);
    }

    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        List<T> results = new ArrayList<T>();

        if (!rs.next()) {
            return results;
        }

        PropertyDescriptor[] props = this.propertyDescriptors(type);
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        do {
            results.add(this.createBean(rs, type, props, columnToProperty));
        } while (rs.next());

        return results;
    }

    /**
     * Returns a PropertyDescriptor[] for the given Class
     *
     * @param c The Class to retrieve PropertyDescriptors for
     * @return A PropertyDescriptor[] describing the Class
     * @throws SQLException if introspection failed
     */
    private PropertyDescriptor[] propertyDescriptors(Class<?> c)
            throws SQLException {
        // Introspector caches BeanInfo classes for better performance
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);

        } catch (IntrospectionException e) {
            throw new SQLException(
                    "Bean introspection failed: " + e.getMessage());
        }

        return beanInfo.getPropertyDescriptors();
    }

    /**
     * 该方法返回一个数组，数组包含两个信息:
     * 一个是数组元素在数组中的位置：这个位置信息表示的是ResultSet中某个字段的位置；
     * 另一个信息就是数组中元素的值：该值存在的是PropertyDescriptor[]数组中的一个索引，该索引用来标识一个PropertyDescriptor，该PropertyDescriptor保存的属性字段信息与当前元素位置表示的那个Column是对应的。
     * @param rsmd
     * @param props
     * @return
     * @throws SQLException
     */
    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd,
                                           PropertyDescriptor[] props) throws SQLException {

        int cols = rsmd.getColumnCount();
        int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }
            String propertyName = columnToPropertyOverrides.get(columnName);
            if (propertyName == null) {
                propertyName = columnName;
            }
            for (int i = 0; i < props.length; i++) {

                if (propertyName.equalsIgnoreCase(props[i].getName())) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }


    /**
     * 该方法的作用才是真正的创建Bean对象的过程
     * @param rs
     * @param type
     * @param props
     * @param columnToProperty
     * @param <T>
     * @return
     * @throws SQLException
     */
    private <T> T createBean(ResultSet rs, Class<T> type,
                             PropertyDescriptor[] props, int[] columnToProperty)
            throws SQLException {

        T bean = this.newInstance(type);

        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            PropertyDescriptor prop = props[columnToProperty[i]];
            Class<?> propType = prop.getPropertyType();

            Object value = this.processColumn(rs, i, propType);

            if (propType != null && value == null && propType.isPrimitive()) {
                value = primitiveDefaults.get(propType);
            }

            this.callSetter(bean, prop, value);
        }

        return bean;
    }

    /**
     * 创建对象
     * @param c
     * @param <T>
     * @return
     * @throws SQLException
     */
    protected <T> T newInstance(Class<T> c) throws SQLException {
        try {
            return c.newInstance();

        } catch (InstantiationException e) {
            throw new SQLException(
                    "Cannot create " + c.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                    "Cannot create " + c.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 该方法的主要作用就是根据bean属性的类型以及对应该属性的Column返回用户期望的值
     * @param rs
     * @param index
     * @param propType
     * @return
     * @throws SQLException
     */
    protected Object processColumn(ResultSet rs, int index, Class<?> propType)
            throws SQLException {

        if ( !propType.isPrimitive() && rs.getObject(index) == null ) {
            return null;
        }

        if (propType.equals(String.class)) {
            return rs.getString(index);

        } else if (
                propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
            return Integer.valueOf(rs.getInt(index));

        } else if (
                propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
            return Boolean.valueOf(rs.getBoolean(index));

        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            return Long.valueOf(rs.getLong(index));

        } else if (
                propType.equals(Double.TYPE) || propType.equals(Double.class)) {
            return Double.valueOf(rs.getDouble(index));

        } else if (
                propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            return Float.valueOf(rs.getFloat(index));

        } else if (
                propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            return Short.valueOf(rs.getShort(index));

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            return Byte.valueOf(rs.getByte(index));

        } else if (propType.equals(Timestamp.class)) {
            return rs.getTimestamp(index);

        } else if (propType.equals(SQLXML.class)) {
            return rs.getSQLXML(index);

        } else {
            return rs.getObject(index);
        }
    }

    /**
     * 当数据获取之后，就需要给bean的属性赋值了
     * @param target
     * @param prop
     * @param value
     * @throws SQLException
     */
    private void callSetter(Object target, PropertyDescriptor prop, Object value)
            throws SQLException {

        Method setter = prop.getWriteMethod();

        if (setter == null) {
            return;
        }

        Class<?>[] params = setter.getParameterTypes();
        try {
            // convert types for some popular ones
            if (value instanceof java.util.Date) {
                final String targetType = params[0].getName();
                if ("java.sql.Date".equals(targetType)) {
                    value = new java.sql.Date(((java.util.Date) value).getTime());
                } else
                if ("java.sql.Time".equals(targetType)) {
                    value = new Time(((java.util.Date) value).getTime());
                } else
                if ("java.sql.Timestamp".equals(targetType)) {
                    value = new Timestamp(((java.util.Date) value).getTime());
                }
            }

            // Don't call setter if the value object isn't the right type
            if (this.isCompatibleType(value, params[0])) {
                setter.invoke(target, new Object[]{value});
            } else {
                throw new SQLException(
                        "Cannot set " + prop.getName() + ": incompatible types, cannot convert "
                                + value.getClass().getName() + " to " + params[0].getName());
                // value cannot be null here because isCompatibleType allows null
            }

        } catch (IllegalArgumentException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    private boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            return true;

        } else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            return true;

        } else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            return true;

        } else if (type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            return true;

        } else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            return true;

        }
        return false;

    }

}
