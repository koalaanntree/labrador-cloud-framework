package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 设置用户角色请求
 *
 * @author ray
 */
@Data
@ToString
public class UserRoleSetRequest implements Serializable {

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "角色id", required = true)
    private String roleId;
}
