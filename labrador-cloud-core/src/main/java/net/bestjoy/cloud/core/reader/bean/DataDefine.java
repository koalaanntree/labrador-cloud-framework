package net.bestjoy.cloud.core.reader.bean;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bestjoy.cloud.core.reader.enums.DataTypeEnum;

/***
 * 数据定义
 * @author ray
 */
@Data
@RequiredArgsConstructor
public class DataDefine {
    /***
     * 字段名称
     */
    @NonNull
    private String name;
    /**
     * 数据类型
     */
    @NonNull
    private DataTypeEnum dataType;
    /**
     * 是否非空校验
     */
    private boolean nullValidate = false;
}
