package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/***
 * 权限操作设置请求
 * @author ray
 */
@Data
@ToString
public class PermissionOperationSetRequest implements Serializable {

    @ApiModelProperty("权限id")
    private String permissionId;

    @ApiModelProperty("操作id")
    private String operationId;
}
