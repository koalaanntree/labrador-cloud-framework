package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 角色权限
 * 定义角色拥有的权限
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@TableName("sys_role_permission_rel")
public class RolePermission extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 角色id
     */
    @NonNull
    private String roleId;

    /***
     * 权限id
     */
    @NonNull
    private String permissionId;
}
