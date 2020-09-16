package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 角色权限删除请求
 * @author ray
 */
@Data
@ToString
public class RolePermissionDeleteRequest implements Serializable {

    @NotBlank(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id", required = true)
    private String roleId;

    @NotBlank(message = "权限id不能为空")
    @ApiModelProperty(value = "权限id", required = true)
    private String permissionId;
}
