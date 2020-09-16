package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 目录权限删除请求
 *
 * @author ray
 */
@Data
@ToString
public class MenuPermissionDeleteRequest {
    @NotBlank(message = "目录id不能为空")
    @ApiModelProperty(value = "目录id", required = true)
    private String menuId;

    @NotBlank(message = "权限id不能为空")
    @ApiModelProperty(value = "权限id", required = true)
    private String permissionId;
}
