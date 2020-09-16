package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 角色
 * @author ray
 */
@Data
@ToString
@TableName("sys_role")
public class Role extends BaseEntity<Long> {
    /**
     * 系统id
     */
    @TableField("system_id")
    private String systemId;
    /**
     * 角色id
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;

    /***
     * 角色code，唯一
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 描述
     */
    private String description;
}
