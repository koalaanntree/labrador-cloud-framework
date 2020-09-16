package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/***
 * 新增或更新角色
 * @author ray
 */
@Data
@ToString
public class SaveOrUpdateRoleRequest implements Serializable {

    @ApiModelProperty("角色id，修改时必传")
    private String roleId;

    @ApiModelProperty(value = "角色名", required = true)
    @NotBlank(message = "角色名不能为空")
    private String roleName;

    @NotBlank(message = "角色code不能为空")
    @ApiModelProperty(value = "角色码，检查唯一性", required = true)
    private String roleCode;

    @ApiModelProperty("描述")
    private String description;
}
