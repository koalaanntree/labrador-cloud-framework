package net.bestjoy.cloud.core.reader;

import java.util.List;
import java.util.Map;

/***
 * 数据读取接口
 *
 * @author ray
 */
public interface DataReader {
    /***
     * 解析为map
     * @return
     */
    List<Map<String, Object>> resolve();

    /***
     * 解析为对象
     * @param clazz
     * @return
     */
    <T> List<T> resolveToBean(Class<T> clazz);
}
