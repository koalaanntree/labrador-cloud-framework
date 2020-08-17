package net.bestjoy.cloud.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.core.generator.IDGenerator;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;
import net.bestjoy.cloud.security.persistent.repository.ElementMapper;
import net.bestjoy.cloud.security.persistent.repository.ElementPermissionMapper;
import net.bestjoy.cloud.security.persistent.repository.PermissionMapper;
import net.bestjoy.cloud.security.persistent.repository.RolePermissionMapper;
import net.bestjoy.cloud.security.service.PermissionSubjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static net.bestjoy.cloud.security.core.error.AuthErrors.ELEMENT_NOT_FOUND_ERROR;


/**
 * @author ray
 */
@Service
@Slf4j
public class PermissionElementProvider implements PermissionSubjectProvider<Element> {
    @Autowired
    private ElementMapper elementMapper;
    @Autowired
    private ElementPermissionMapper elementPermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public Element getPermissionSubject(String subjectId) {
        QueryWrapper<Element> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("element_id", subjectId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());
        return elementMapper.selectOne(queryWrapper);
    }

    @Override
    public void updatePermissionSubject(Element element) {
        element.setUpdateTime(new Date());
        elementMapper.updateById(element);
    }

    @Override
    public void savePermissionSubject(Element element) {
        element.setCreateTime(new Date());
        element.setUpdateTime(element.getCreateTime());
        element.setElementId(IDGenerator.SNOW_FLAKE_STRING.generate());
        element.setSystemId(SecurityContext.getSystemId());

        elementMapper.insert(element);
    }

    @Override
    public Element getPermissionSubjectByName(String subjectName) {
        QueryWrapper<Element> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("element_name", subjectName);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());
        return elementMapper.selectOne(queryWrapper);
    }

    @Override
    public PermissionTypeEnum getPermissionType() {
        return PermissionTypeEnum.ELEMENT;
    }

    @Override
    public boolean hasPermissionSubject(String subjectId) {
        Optional<Element> optionalElement = Optional.ofNullable(getPermissionSubject(subjectId));
        optionalElement.orElseThrow(() -> new BusinessException(ELEMENT_NOT_FOUND_ERROR));

        return true;
    }

    @Override
    public void addPermissionSubjectRel(String subjectId, String permissionId) {
        QueryWrapper<ElementPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("element_id", subjectId);
        queryWrapper.eq("permission_id", permissionId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        ElementPermission elementPermission = elementPermissionMapper.selectOne(queryWrapper);

        if (elementPermission == null) {
            elementPermission = new ElementPermission(subjectId, permissionId);
            elementPermission.setCreateTime(new Date());
            elementPermission.setUpdateTime(elementPermission.getCreateTime());
            elementPermission.setSystemId(SecurityContext.getSystemId());
            log.info("元素 “{}” 新增 “{}” 权限", subjectId, permissionId);
            elementPermissionMapper.insert(elementPermission);
        }
    }

    @Override
    public void deletePermissionSubjectRel(String subjectId, String permissionId) {
        QueryWrapper<ElementPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("element_id", subjectId);
        queryWrapper.eq("permission_id", permissionId);
        queryWrapper.eq("system_id", SecurityContext.getSystemId());

        elementPermissionMapper.delete(queryWrapper);
    }

    @Override
    public List<Role> getSubjectRequireRoleList(String subjectId) {
        return elementPermissionMapper.getElementRequireRoleList(subjectId);
    }

    @Override
    public List<Permission> getSubjectPermissionList(String subjectId) {
        return elementPermissionMapper.getElementPermissionList(subjectId);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteSubject(String subjectId) {
        //删除页面元素
        QueryWrapper<Element> elementQueryWrapper = new QueryWrapper<>();
        elementQueryWrapper.eq("element_id", subjectId);
        elementQueryWrapper.eq("system_id", SecurityContext.getSystemId());
        elementMapper.delete(elementQueryWrapper);


        QueryWrapper<ElementPermission> elementPermissionQueryWrapper = new QueryWrapper<>();
        elementPermissionQueryWrapper.eq("element_id", subjectId);
        elementPermissionQueryWrapper.eq("system_id", SecurityContext.getSystemId());
        List<ElementPermission> elementPermissionList = elementPermissionMapper.selectList(elementPermissionQueryWrapper);

        if (CollectionUtils.isEmpty(elementPermissionList)) {
            return;
        }
        //删除页面元素权限
        elementPermissionMapper.delete(elementPermissionQueryWrapper);

        List<String> permissionIdList = new ArrayList<>();
        elementPermissionList.forEach(elementPermission -> {
            permissionIdList.add(elementPermission.getPermissionId());
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
