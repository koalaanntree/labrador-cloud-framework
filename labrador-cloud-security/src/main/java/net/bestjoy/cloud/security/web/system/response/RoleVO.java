package net.bestjoy.cloud.security.web.system.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.security.core.entitiy.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * 角色返回结果
 * @author ray
 */
@Data
@ToString
public class RoleVO implements Serializable {
    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("角色名")
    private String roleName;

    @ApiModelProperty("角色码")
    private String roleCode;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;
//
//    public static RoleVO convert(Role role) {
//        if (role == null) {
//            return null;
//        }
//
//        RoleVO response = new RoleVO();
//        BeanUtils.copyProperties(role, response);
//
//        return response;
//    }
//
//    public static List<RoleVO> convert(List<Role> roles) {
//
//        List<RoleVO> list = new ArrayList<>();
//        if (CollectionUtils.isEmpty(roles)) {
//            return list;
//        }
//
//        roles.forEach(role -> {
//            list.add(convert(role));
//        });
//
//        return list;
//    }
}
