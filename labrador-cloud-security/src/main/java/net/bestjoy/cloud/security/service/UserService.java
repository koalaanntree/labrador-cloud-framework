package net.bestjoy.cloud.security.service;


import net.bestjoy.cloud.security.core.entitiy.*;

import java.util.List;

/***
 * 用户持久层接口
 * @author ray
 */
public interface UserService {

    /***
     * 根据用户查找
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 根据userId查找
     *
     * @param userId
     * @return
     */
    User getUserByUserId(String userId);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);

    /***
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    List<Role> getUserRoles(String userId);

    /***
     * 新增用户角色关系
     * @param userRole
     */
    void addUserRole(UserRole userRole);

    /***
     * 删除用户角色关系
     * @param userId
     * @param roleId
     */
    void deleteUserRole(String userId, String roleId);

    /***
     * 获取用户拥有的权限id列表
     * @param userId
     * @return
     */
    List<String> getUserPermissionIdList(String userId);

    /***
     * 获取用户用户的权限列表
     * @param userId
     * @return
     */
    List<Permission> getUserPermissionList(String userId);
}
