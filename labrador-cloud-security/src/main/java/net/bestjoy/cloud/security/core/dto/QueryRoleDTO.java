package net.bestjoy.cloud.security.core.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.bean.QueryCondition;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.Role;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/***
 * 查询角色DTO
 * @author ray
 */
@Data
@ToString
public class QueryRoleDTO implements Serializable, QueryCondition<Role> {

    @ApiModelProperty("角色ID")
    private String roleId;

    @ApiModelProperty("角色名称，模糊查询")
    private String roleName;

    @ApiModelProperty("角色code,模糊查询")
    private String roleCode;

    @Override
    public QueryWrapper<Role> buildQueryCondition() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Role::getSystemId, SecurityContext.getSystemId());

        if (StringUtils.isNotBlank(roleId)) {
            queryWrapper.lambda().eq(Role::getRoleId, roleId);
        }

        if (StringUtils.isNotBlank(roleName)) {
            queryWrapper.lambda().like(Role::getRoleName, roleName);
        }

        if (StringUtils.isNotBlank(roleCode)) {
            queryWrapper.lambda().like(Role::getRoleCode, roleCode);
        }

        queryWrapper.lambda().orderByDesc(Role::getCreateTime);

        return queryWrapper;
    }
}
