package net.bestjoy.cloud.security.persistent.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bestjoy.cloud.security.core.entitiy.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ray
 */
@Repository
public interface MenuPermissionMapper extends BaseMapper<MenuPermission> {
    /***
     * 获取目录所需的角色列表
     * @param menuId
     * @return
     */
    @Select("select role.* from sys_menu_permission_rel menu_rel LEFT JOIN sys_permission perm on menu_rel.permission_id = perm.permission_id \n" +
            "LEFT JOIN sys_role_permission_rel role_rel on role_rel.permission_id = perm.permission_id\n" +
            "LEFT JOIN sys_role role on role.role_id = role_rel.role_id\n" +
            "where menu_rel.menu_id = #{menuId}")
    List<Role> getMenuRequireRoleList(@Param("menuId") String menuId);

    @Select("select perm.* from sys_menu_permission_rel menu_rel LEFT JOIN sys_permission perm on menu_rel.permission_id = perm.permission_id \n" +
            "LEFT JOIN sys_role_permission_rel role_rel on role_rel.permission_id = perm.permission_id\n" +
            "where menu_rel.menu_id = #{menuId}")
    List<Permission> getMenuPermissionList(@Param("menuId") String menuId);
}
