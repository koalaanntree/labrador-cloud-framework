package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.entity.BaseEntity;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;

/***
 * 权限
 * 定义对什么（menu/element/file）进行什么样动作(operation)
 *
 * @author ray
 */
@Data
@ToString
@TableName("sys_permission")
public class Permission extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 权限名
     */
    private String permissionName;

    /**
     * 权限类别
     */
    @EnumValue
    private PermissionTypeEnum permissionType;

    /**
     * 权限描述
     */
    private String description;
}
