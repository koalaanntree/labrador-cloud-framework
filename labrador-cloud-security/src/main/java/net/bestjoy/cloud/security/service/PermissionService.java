package net.bestjoy.cloud.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.bestjoy.cloud.core.bean.PageBean;
import net.bestjoy.cloud.security.core.dto.QueryRoleDTO;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.MenuTypeEnum;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;

import java.util.List;

/**
 * @author ray
 */
public interface PermissionService {

    /**
     * 获取角色
     *
     * @param roleId
     * @return
     */
    Role getRoleById(String roleId);

    /**
     * 根据角色码，唯一查找角色
     *
     * @param roleCode
     * @return
     */
    Role getRoleByCode(String roleCode);

    /***
     * 分页查找角色列表
     * @param queryRoleDTO
     * @param pageBean
     * @return
     */
    IPage<Role> pageQueryRoles(QueryRoleDTO queryRoleDTO, PageBean<Role> pageBean);

    /**
     * 添加角色
     *
     * @param role
     */
    void addRole(Role role);

    /***
     * 更新角色
     * @param role
     */
    void updateRole(Role role);

    /**
     * 删除角色
     * 会删除角色关联的用户
     *
     * @param roleId
     */
    void deleteRole(String roleId);

    /***
     * 获取所有的菜单列表，不分页
     * @param parentMenuId
     * @param menuType
     * @return
     */
    List<Menu> getPublishedMenuList(String parentMenuId, MenuTypeEnum menuType);

    /**
     * 保存权限
     *
     * @param permission     权限
     * @param permissionType 权限类别
     * @param subjectId      权限对象
     * @param operationId    权限操作
     */
    void savePermission(Permission permission, PermissionTypeEnum permissionType, String subjectId, String operationId);

    /**
     * 更新权限
     *
     * @param permission
     */
    void updatePermission(Permission permission);

    /***
     * 权限码唯一查找权限
     * @param permissionCode
     * @return
     */
    Permission getPermissionByCode(String permissionCode);

    /**
     * 根据权限id唯一查找
     *
     * @param permissionId
     * @return
     */
    Permission getPermissionByPermissionId(String permissionId);

    /**
     * 保存角色权限
     *
     * @param roleId
     * @param permissionId
     */
    void addRolePermission(String roleId, String permissionId);

    /**
     * 删除角色权限
     *
     * @param roleId
     * @param permissionId
     */
    void deleteRolePermission(String roleId, String permissionId);

    /***
     * 查询角色权限列表
     * @param page
     * @param roleId
     * @return
     */
    IPage<Permission> queryRolePermission(Page<Permission> page, String roleId);

    /**
     * 权限操作是否存在
     *
     * @param operationId
     * @return
     */
    boolean hasOperation(String operationId);

    /**
     * 保存操作
     *
     * @param operation
     */
    void saveOperation(Operation operation);

    /***
     * 更新操作
     * @param operation
     */
    void updateOperation(Operation operation);

    /***
     * 新增权限操作
     * @param permissionId
     * @param operationId
     */
    void addPermissionOperation(String permissionId, String operationId);

    /***
     * 删除权限操作
     * @param permissionId
     * @param operationId
     */
    void deletePermissionOperation(String permissionId, String operationId);

    /**
     * 根据权限id查询唯一
     *
     * @param operationId
     * @return
     */
    Operation getOperationById(String operationId);

    /***
     * 根据操作码唯一查找
     * @param operationCode
     * @return
     */
    Operation getOperationByCode(String operationCode);
}
