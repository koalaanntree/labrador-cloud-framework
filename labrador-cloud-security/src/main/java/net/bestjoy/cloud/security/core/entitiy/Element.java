package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;
import net.bestjoy.cloud.security.core.enums.ElementTypeEnum;

/**
 * 页面元素
 *
 * @author ray
 */
@Data
@ToString
@TableName("sys_element")
public class Element extends BaseEntity<Long> {
    /**
     * 系统id
     */
    @TableField("system_id")
    private String systemId;

    /**
     * 元素id
     */
    @TableField("element_id")
    private String elementId;

    /**
     * 元素名称
     */
    @TableField("element_name")
    private String elementName;

    /***
     * 元素code
     */
    @TableField("element_code")
    private String elementCode;
    /**
     * 页面元素类型
     */
    @EnumValue
    @TableField("element_type")
    private ElementTypeEnum elementType;

    /***
     * 扩展
     */
    @TableField("ext_info")
    private String extInfo;

    /**
     * 元素描述
     */
    private String description;
}
