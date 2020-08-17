package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
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
    private String systemId;

    /**
     * 元素id
     */
    private String elementId;

    /**
     * 元素名称
     */
    private String elementName;

    /**
     * 页面元素类型
     */
    @EnumValue
    private ElementTypeEnum elementType;

    /***
     * 扩展
     */
    private String extInfo;

    /**
     * 元素描述
     */
    private String description;
}
