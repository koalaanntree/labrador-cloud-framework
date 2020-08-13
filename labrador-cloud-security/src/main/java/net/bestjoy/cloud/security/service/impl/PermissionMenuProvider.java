package net.bestjoy.cloud.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.core.generator.IDGenerator;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;
import net.bestjoy.cloud.security.persistent.repository.*;
import net.bestjoy.cloud.security.service.PermissionSubjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.bestjoy.cloud.security.core.error.AuthErrors.MENU_NOT_FOUND_ERROR;

/**
 * 权限主体目录
 *
 * @author ray
 */
@Service
@Slf4j
public class PermissionMenuProvider implements PermissionSubjectProvider<Menu> {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuPermissionMapper menuPermissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Menu getPermissionSubject(String subjectId) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id", subjectId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        return menuMapper.selectOne(queryWrapper);
    }

    @Override
    public PermissionTypeEnum getPermissionType() {
        return PermissionTypeEnum.MENU;
    }

    @Override
    public boolean hasPermissionSubject(String subjectId) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id", subjectId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        Menu menu = menuMapper.selectOne(queryWrapper);

        if (menu == null) {
            throw new BusinessException(MENU_NOT_FOUND_ERROR);
        }
        return true;
    }

    @Override
    public void addPermissionSubjectRel(String subjectId, String permissionId) {
        QueryWrapper<MenuPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id", subjectId);
        queryWrapper.eq("permission_id", permissionId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        MenuPermission menuPermission = menuPermissionMapper.selectOne(queryWrapper);
        if (menuPermission == null) {
            menuPermission = new MenuPermission(subjectId, permissionId);
            menuPermission.setCreateTime(new Date());
            menuPermission.setUpdateTime(menuPermission.getCreateTime());
            menuPermission.setSystemId(SecurityContext.getSystemId());
            log.info("新增菜单 ‘{}’ 的权限 ‘{}’", subjectId, permissionId);
            menuPermissionMapper.insert(menuPermission);
        }
    }

    @Override
    public void updatePermissionSubject(Menu menu) {
        menu.setUpdateTime(new Date());

        menuMapper.updateById(menu);
    }

    @Override
    public void savePermissionSubject(Menu menu) {
        menu.setCreateTime(new Date());
        menu.setUpdateTime(menu.getCreateTime());
        if (menu.getMenuStatus() == null) {
            menu.setMenuStatus(MenuStatusEnum.EDITTING);
        }
        menu.setMenuId(IDGenerator.SNOW_FLAKE_STRING.generate());
        menu.setSystemId(SecurityContext.getSystemId());
        menuMapper.insert(menu);
    }

    @Override
    public Menu getPermissionSubjectByName(String subjectName) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_name", subjectName);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        return menuMapper.selectOne(queryWrapper);
    }

    @Override
    public void deletePermissionSubjectRel(String subjectId, String permissionId) {
        QueryWrapper<MenuPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id", subjectId);
        queryWrapper.eq("permission_id", permissionId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        menuPermissionMapper.delete(queryWrapper);
    }

    @Override
    public List<Role> getSubjectRequireRoleList(String subjectId) {
        return menuPermissionMapper.getMenuRequireRoleList(subjectId);
    }

    @Override
    public List<Permission> getSubjectPermissionList(String subjectId) {
        return menuPermissionMapper.getMenuPermissionList(subjectId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteSubject(String subjectId) {
        //删除目录
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("menu_id", subjectId);
        menuQueryWrapper.eq("system_id", SecurityContext.getSystemId());
        menuMapper.delete(menuQueryWrapper);

        QueryWrapper<MenuPermission> menuPermissionQueryWrapper = new QueryWrapper<>();

        menuPermissionQueryWrapper.eq("menu_id", subjectId);
        menuPermissionQueryWrapper.eq("system_id", SecurityContext.getSystemId());
        List<MenuPermission> menuPermissionList = menuPermissionMapper.selectList(menuPermissionQueryWrapper);

        if (CollectionUtils.isEmpty(menuPermissionList)) {
            return;
        }
        //删除目录相关权限
        menuPermissionMapper.delete(menuPermissionQueryWrapper);

        List<String> permissionIdList = new ArrayList<>();
        menuPermissionList.forEach(menuPermission -> {
            permissionIdList.add(menuPermission.getPermissionId());
        });

        //删除权限
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.in("permission_id", permissionIdList);
        permissionQueryWrapper.eq("system_id", SecurityContext.getSystemId());
        permissionMapper.delete(permissionQueryWrapper);

        //删除角色权限
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.in("permission_id", permissionIdList);
        rolePermissionQueryWrapper.eq("system_id", SecurityContext.getSystemId());
        rolePermissionMapper.delete(rolePermissionQueryWrapper);
    }
}
