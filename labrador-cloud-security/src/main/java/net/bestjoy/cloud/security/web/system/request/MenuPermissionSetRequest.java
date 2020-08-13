package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/***
 * 目录权限设置请求
 * @author ray
 */
@Data
@ToString
public class MenuPermissionSetRequest implements Serializable {

    @ApiModelProperty(value = "目录id", required = true)
    private String menuId;

    @ApiModelProperty(value = "权限id", required = true)
    private String permissionId;
}
