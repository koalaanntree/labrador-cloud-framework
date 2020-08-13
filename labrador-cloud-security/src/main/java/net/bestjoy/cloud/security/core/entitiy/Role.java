package net.bestjoy.cloud.security.core.entitiy;

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
    private String systemId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 描述
     */
    private String description;
}
