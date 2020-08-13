package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.entitiy.Permission;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 权限返回结果
 *
 * @author ray
 */
@Data
@ToString
public class PermissionResponse implements Serializable {
    @ApiModelProperty("权限id")
    private String permissionId;

    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("权限描述")
    private String description;

    @ApiModelProperty("权限类别")
    private PermissionTypeEnum permissionType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public static PermissionResponse convert(Permission permission) {
        if (permission == null) {
            return null;
        }

        PermissionResponse response = new PermissionResponse();
        BeanUtils.copyProperties(permission, response);
        return response;
    }

    public static List<PermissionResponse> convert(List<Permission> permissionList) {

        List<PermissionResponse> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(permissionList)) {
            return list;
        }

        permissionList.forEach(permission -> {
            list.add(convert(permission));
        });

        return list;
    }
}
