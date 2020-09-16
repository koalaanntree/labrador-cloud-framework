package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 删除用户角色请求
 * @author ray
 */
@Data
@ToString
public class UserRoleDeleteRequest implements Serializable {

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @NotBlank(message = "角色Id不能为空")
    @ApiModelProperty(value = "角色id", required = true)
    private String roleId;

}
