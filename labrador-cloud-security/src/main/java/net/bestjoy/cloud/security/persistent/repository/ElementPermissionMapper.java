package net.bestjoy.cloud.security.persistent.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bestjoy.cloud.security.core.entitiy.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @author ray
 */
@Repository
public interface ElementPermissionMapper extends BaseMapper<ElementPermission> {
    /***
     * 获取目录所需的角色列表
     * @param elementId
     * @return
     */
    @Select("select element.* from sys_element_permission_rel element_rel LEFT JOIN sys_permission perm on element_rel.permission_id = perm.permission_id \n" +
            "LEFT JOIN sys_role_permission_rel role_rel on role_rel.permission_id = perm.permission_id\n" +
            "LEFT JOIN sys_role role on role.role_id = role_rel.role_id\n" +
            "where element_rel.element_id = #{elementId}")
    List<Role> getElementRequireRoleList(@Param("elementId") String elementId);

    @Select("select perm.* from sys_element_permission_rel element_rel LEFT JOIN sys_permission perm on element_rel.permission_id = perm.permission_id \n" +
            "LEFT JOIN sys_role_permission_rel role_rel on role_rel.permission_id = perm.permission_id\n" +
            "where element_rel.elementId = #{elementId}")
    List<Permission> getElementPermissionList(@Param("elementId") String elementId);
}
