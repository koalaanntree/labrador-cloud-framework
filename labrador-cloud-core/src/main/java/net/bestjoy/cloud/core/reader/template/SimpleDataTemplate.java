package net.bestjoy.cloud.core.reader.template;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.bestjoy.cloud.core.reader.bean.DataDefine;
import net.bestjoy.cloud.core.reader.enums.DataTypeEnum;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/***
 * 简单数据模板
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
public class SimpleDataTemplate<T> {
    /**
     * 起始行，默认0开始
     */
    private Integer startRow = 0;
    /***
     * 起始列，默认0开始
     */
    private Integer startColumn = 0;
    /***
     * 数据集合，注意排序
     */
    private List<DataDefine> dataDefines;

    public SimpleDataTemplate(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();

        List<DataDefine> dataDefines = new ArrayList<>();

        for (Field field : fields) {
            DataTypeEnum dataType;
            if (String.class.equals(field.getType())) {
                dataType = DataTypeEnum.STRING;
            } else {
                dataType = DataTypeEnum.NUMBER;
            }
            DataDefine dataDefine = new DataDefine(field.getName(), dataType);
            //todo  nullable
            dataDefines.add(dataDefine);
        }

        this.dataDefines = dataDefines;
    }
}
