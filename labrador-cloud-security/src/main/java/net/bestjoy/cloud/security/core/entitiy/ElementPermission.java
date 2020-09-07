package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 页面元素权限
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@TableName("sys_element_permission_rel")
public class ElementPermission extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 页面元素id
     */
    @NonNull
    private String elementId;

    /***
     * 权限id
     */
    @NonNull
    private String permissionId;
}
