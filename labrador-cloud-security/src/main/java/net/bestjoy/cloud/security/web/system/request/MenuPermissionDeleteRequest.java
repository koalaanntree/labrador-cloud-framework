package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 目录权限删除请求
 *
 * @author ray
 */
@Data
@ToString
public class MenuPermissionDeleteRequest {
    @ApiModelProperty(value = "目录id", required = true)
    private String menuId;

    @ApiModelProperty(value = "权限id", required = true)
    private String permissionId;
}
