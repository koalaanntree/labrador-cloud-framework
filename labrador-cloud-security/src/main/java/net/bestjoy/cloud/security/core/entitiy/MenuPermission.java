package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 目录权限
 * 定义权限对象为目录
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@TableName("sys_menu_permission_rel")
public class MenuPermission extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;
    /***
     * 目录id
     */
    @NonNull
    private String menuId;

    /***
     * 权限id
     */
    @NonNull
    private String permissionId;
}
