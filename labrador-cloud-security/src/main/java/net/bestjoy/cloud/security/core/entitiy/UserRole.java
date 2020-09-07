package net.bestjoy.cloud.security.core.entitiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.bestjoy.cloud.core.entity.BaseEntity;

/***
 * 用户角色关系
 * @author ray
 */
@Data
@ToString
@TableName("sys_user_role_rel")
@NoArgsConstructor
@RequiredArgsConstructor
public class UserRole extends BaseEntity<Long> {
    /**
     * 系统id
     */
    private String systemId;

    /**
     * 用户id
     */
    @NonNull
    private String userId;

    /***
     * 角色id
     */
    @NonNull
    private String roleId;
}
