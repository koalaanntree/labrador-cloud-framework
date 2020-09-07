package net.bestjoy.cloud.core.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

import java.util.HashMap;
import java.util.Map;

/***
 * 切面工具类
 * @author ray
 */
public class AspectUtil {

    /**
     * 获取参数列表
     *
     * @param joinPoint
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> getFieldsName(JoinPoint joinPoint) {

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        //参数名
        String[] parameterNames = codeSignature.getParameterNames();
        // 参数值
        Object[] args = joinPoint.getArgs();
        Map<String, Object> paramMap = new HashMap<>(32);
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }
}
