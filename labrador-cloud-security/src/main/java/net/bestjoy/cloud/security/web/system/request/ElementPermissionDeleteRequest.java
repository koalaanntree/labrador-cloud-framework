package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 页面元素权限删除请求
 * @author ray
 */
@Data
@ToString
public class ElementPermissionDeleteRequest implements Serializable {
    @NotBlank(message = "页面元素Id不能为空")
    @ApiModelProperty(value = "页面元素id", required = true)
    private String elementId;

    @NotBlank(message = "权限Id不能为空")
    @ApiModelProperty(value = "权限id", required = true)
    private String permissionId;
}
