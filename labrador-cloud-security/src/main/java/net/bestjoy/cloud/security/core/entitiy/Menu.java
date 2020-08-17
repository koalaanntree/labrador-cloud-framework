package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import net.bestjoy.cloud.security.core.enums.MenuTypeEnum;

/***
 * 目录
 * @author ray
 */
@Data
@ToString
@TableName("sys_menu")
public class Menu extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 目录id
     */
    private String menuId;

    /**
     * 菜单类型
     */
    @EnumValue
    private MenuTypeEnum menuType;

    /**
     * 目录名称
     */
    private String menuName;

    /**
     * 目录url
     */
    private String menuUrl;

    /**
     * 目录描述
     */
    private String description;

    /**
     * 目录状态
     */
    @EnumValue
    private MenuStatusEnum menuStatus;

    /***
     * 排序用
     */
    private Integer orderNum;

    /**
     * 父目录id
     */
    private String parentMenuId;
}
