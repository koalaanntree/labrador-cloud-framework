package net.bestjoy.cloud.security.web.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 保存或更新权限请求
 *
 * @author ray
 */
@Data
@ToString
public class SaveOrUpdatePermissionRequest implements Serializable {
    @ApiModelProperty("权限id，更新时必传")
    private String permissionId;

    @NotBlank(message = "权限名称不能为空")
    @ApiModelProperty(value = "权限名称", required = true)
    private String permissionName;

    @NotBlank(message = "权限码不能为空")
    @ApiModelProperty(value = "权限吗,唯一校验", required = true)
    private String permissionCode;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("权限类别，指对什么类型的权限，新增时必填，需要subjectId对应")
    private PermissionTypeEnum permissionType;

    @ApiModelProperty("权限类型指定的主体id，如果是菜单menuId，如果是页面元素elementId等，新增时必须指定是对什么的权限；更新不支持，需要单独操作")
    private String subjectId;

    @ApiModelProperty("操作id，新增时必填；更新不支持，需要单独操作")
    private String operationId;

    @ApiModelProperty("角色ID")
    private String roleId;
}
