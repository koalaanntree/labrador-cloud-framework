package net.bestjoy.cloud.core.util;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.error.bean.Errors;
import net.bestjoy.cloud.error.bean.SysException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean工具类
 *
 * @date 2018/6/22 15:30
 */
@Slf4j
public final class BeanUtils {

    /**
     * bean转map
     */
    public static Map<String, Object> beanToMap(Object obj) {
        PropertyDescriptor[] propertyDescriptors = org.springframework.beans.BeanUtils
                .getPropertyDescriptors(obj.getClass());

        Map<String, Object> map = new HashMap<>(propertyDescriptors.length);
        PropertyDescriptor descriptor = null;
        try {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                descriptor = propertyDescriptors[i];
                if (descriptor.getReadMethod().toString().indexOf(" native") > 0) {
                    continue;
                }
                map.put(descriptor.getDisplayName(), descriptor.getReadMethod().invoke(obj));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "转换错误fieldName:" + descriptor != null ? descriptor.getDisplayName() : "null");
        }

        return map;
    }

    /**
     * 获取所有值
     */
    public static Object[] getValues(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();

        return Arrays.stream(fields).map(field -> {
            field.setAccessible(true);
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                throw new SysException(Errors.Sys.SYS_ERROR, "获取值出错，field : " + field);
            }
        }).toArray();
    }

    /**
     * map转bean
     */
    public static <T, X extends Object> T mapToBean(Map<String, X> map, T t) {
        PropertyDescriptor[] propertyDescriptors = org.springframework.beans.BeanUtils
                .getPropertyDescriptors(t.getClass());

        PropertyDescriptor descriptor = null;
        Method method;
        try {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                descriptor = propertyDescriptors[i];
                method = descriptor.getWriteMethod();
                if (method == null || method.toString().indexOf(" native") > 0) {
                    continue;
                }
                method.invoke(t, map.get(descriptor.getDisplayName()));
            }

            return t;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "转换错误fieldName:" + descriptor != null ? descriptor.getDisplayName() : "null");
        }
    }

    public static <T> T deepClone(T t) {
        T clone = null;
        try {
            clone = (T) SharinganCloneUtils.deepClone(t);
        } catch (Exception e) {
            log.error("deep clone error :{}", e);
        }
        return clone;
    }

}
