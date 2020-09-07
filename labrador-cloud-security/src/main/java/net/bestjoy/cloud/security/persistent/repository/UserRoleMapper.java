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
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /***
     * 获取用户角色列表
     * @param userId
     * @return
     */
    @Select("select rel.user_id, role.* from sys_user_role_rel rel left join sys_role role on rel.role_id = role.role_id where rel.user_id=#{userId} order by rel.create_time desc")
    List<Role> selectUerRoleList(@Param("userId") String userId);

    /***
     * 获取用户权限id列表
     * @param userId
     * @return
     */
    @Select("select role_per_rel.permission_id from sys_user_role_rel user_role_rel LEFT JOIN \n" +
            "sys_role_permission_rel role_per_rel on user_role_rel.role_id = role_per_rel.role_id\n" +
            "where user_role_rel.user_id = #{userId}")
    List<String> selectUserPermissionIdList(@Param("userId") String userId);

    /**
     * 获取用户权限列表
     *
     * @param userId
     * @return
     */
    @Select("select perm.* from sys_user_role_rel user_role_rel LEFT JOIN \n" +
            "sys_role_permission_rel role_per_rel on user_role_rel.role_id = role_per_rel.role_id LEFT JOIN\n" +
            "sys_permission perm on perm.permission_id = role_per_rel.permission_id\n" +
            "where user_role_rel.user_id = #{userId}")
    List<Permission> selectUserPermissionList(@Param("userId") String userId);
}
