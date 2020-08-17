package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

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

    @ApiModelProperty("角色名")
    private String roleName;

    @ApiModelProperty("描述")
    private String description;
}
