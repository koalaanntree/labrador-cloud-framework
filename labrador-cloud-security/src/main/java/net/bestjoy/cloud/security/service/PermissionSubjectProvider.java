package net.bestjoy.cloud.security.service;


import net.bestjoy.cloud.security.core.entitiy.Permission;
import net.bestjoy.cloud.security.core.entitiy.Role;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;

import java.util.List;

/***
 * 权限主体方法
 * @author ray
 */
public interface PermissionSubjectProvider<T> {

    /***
     * 根据id查找主体对象
     * @param subjectId
     * @return
     */
    T getPermissionSubject(String subjectId);

    /***
     * 更新权限主体
     * @param subject
     */
    void updatePermissionSubject(T subject);

    /**
     * 保存权限主体
     *
     * @param subject
     */
    void savePermissionSubject(T subject);

    /**
     * 根据权限主体名称查询
     *
     * @param subjectName
     * @return
     */
    T getPermissionSubjectByName(String subjectName);

    /**
     * 权限类型
     *
     * @return
     */
    PermissionTypeEnum getPermissionType();

    /***
     * 是否存在权限主体对象
     *
     * @param subjectId
     * @return
     */
    boolean hasPermissionSubject(String subjectId);

    /***
     * 新增权限主体对象关系
     * @param subjectId
     * @param permissionId
     */
    void addPermissionSubjectRel(String subjectId, String permissionId);

    /***
     * 删除权限主体对象关系
     * @param subjectId
     * @param permissionId
     */
    void deletePermissionSubjectRel(String subjectId, String permissionId);

    /***
     * 获取权限主体所需的角色列表
     * @param subjectId
     * @return
     */
    List<Role> getSubjectRequireRoleList(String subjectId);

    /**
     * 获取权限主体属实的权限列表
     *
     * @param subjectId
     * @return
     */
    List<Permission> getSubjectPermissionList(String subjectId);

    /**
     * 删除权限主体
     *
     * @param subjectId
     */
    void deleteSubject(String subjectId);
}
