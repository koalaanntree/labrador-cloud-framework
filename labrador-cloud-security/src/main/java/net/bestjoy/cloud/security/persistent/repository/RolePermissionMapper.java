package net.bestjoy.cloud.security.persistent.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bestjoy.cloud.security.core.entitiy.Permission;
import net.bestjoy.cloud.security.core.entitiy.RolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/***
 * @author ray
 */
@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    /***
     * 获取角色的权限列表
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select permission.*, rel.role_id from sys_role_permission_rel rel left join sys_permission permission on rel.permission_id = permission.permission_id  ${ew.getCustomSqlSegment}")
    IPage<Permission> pageRolePermissionOfRole(Page<Permission> page, @Param(Constants.WRAPPER) Wrapper<RolePermission> wrapper);
}
