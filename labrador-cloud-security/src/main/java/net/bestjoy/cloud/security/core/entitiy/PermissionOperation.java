package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 权限操作
 * 定义权限是什么样的操作
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@TableName("sys_permission_operation_rel")
public class PermissionOperation extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 权限id
     */
    @NonNull
    private String permissionId;
    /**
     * 操作id
     */
    @NonNull
    private String operationId;
}
