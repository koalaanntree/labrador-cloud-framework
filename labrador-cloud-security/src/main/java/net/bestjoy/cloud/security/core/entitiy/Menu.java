package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField("system_id")
    private String systemId;
    /**
     * 目录id
     */
    @TableField("menu_id")
    private String menuId;

    /**
     * 菜单类型
     */
    @EnumValue
    @TableField("menu_type")
    private MenuTypeEnum menuType;

    /**
     * 目录名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 目录 code
     */
    @TableField("menu_code")
    private String menuCode;
    /**
     * 目录url
     */
    @TableField("menu_url")
    private String menuUrl;

    /**
     * 目录描述
     */
    private String description;

    /**
     * 目录状态
     */
    @EnumValue
    @TableField("menu_status")
    private MenuStatusEnum menuStatus;

    /***
     * 排序用
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 父目录id
     */
    @TableField("parent_menu_id")
    private String parentMenuId;
}
