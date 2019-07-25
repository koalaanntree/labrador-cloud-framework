package net.bestjoy.cloud.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import net.bestjoy.cloud.core.sensitive.SensitiveInfoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 参数处理类
 * @author ray
 */
public class ParamsUtil {

    private static final List<Class> IGNORE_CLASSES = new ArrayList<>();

    static {
        String[] classes = new String[]{
                "javax.servlet.ServletRequest",
                "javax.servlet.ServletResponse",
                "javax.servlet.http.HttpSession"
        };

        for (int i = 0; i < classes.length; i++) {
            String aClass = classes[i];
            try {
                IGNORE_CLASSES.add(Class.forName(aClass));
            } catch (Exception e) {
            }
        }
    }

    /**
     * 忽略HttpServletRequest&& HttpServletResponse
     * 不记录参数名称，只记录参数顺序，并且会对数据进行脱敏
     */
    public static String paramsToString(String[] parameterNames, Object[] args) {

        if (parameterNames == null || parameterNames.length == 0) {
            return null;
        }

        final PropertyFilter propertyFilter = (source, name, value) -> {
            if (value == null) {
                return true;
            }

            for (Class cls : IGNORE_CLASSES) {
                if (cls.isAssignableFrom(value.getClass())) {
                    return false;
                }
            }

            return true;
        };

        final ValueFilter valueFilter = (object, name, value) -> SensitiveInfoUtils.getJsonObject(value);

        SerializeFilter[] filters = {propertyFilter, valueFilter};

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }

        return JSON.toJSONString(params, filters);
    }
}
