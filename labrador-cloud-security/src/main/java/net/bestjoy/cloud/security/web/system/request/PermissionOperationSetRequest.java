package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 权限操作设置请求
 * @author ray
 */
@Data
@ToString
public class PermissionOperationSetRequest implements Serializable {

    @NotBlank(message = "权限id不能为空")
    @ApiModelProperty(value = "权限id", required = true)
    private String permissionId;

    @NotBlank(message = "操作id不能为空")
    @ApiModelProperty(value = "操作id", required = true)
    private String operationId;
}
