package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/***
 * 角色权限设置请求
 * @author ray
 */
@Data
@ToString
public class RolePermissionSetRequest implements Serializable {

    @ApiModelProperty(value = "角色id", required = true)
    private String roleId;

    @ApiModelProperty(value = "权限id",required = true)
    private String permissionId;
}
