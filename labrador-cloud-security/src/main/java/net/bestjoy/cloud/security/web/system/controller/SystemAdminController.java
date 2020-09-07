package net.bestjoy.cloud.security.web.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.PageBean;
import net.bestjoy.cloud.core.bean.PageData;
import net.bestjoy.cloud.core.bean.PageInfo;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.annotation.IgnoreAuth;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import net.bestjoy.cloud.security.core.enums.MenuTypeEnum;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;
import net.bestjoy.cloud.security.service.PermissionService;
import net.bestjoy.cloud.security.service.PermissionSubjectContext;
import net.bestjoy.cloud.security.service.PermissionSubjectProvider;
import net.bestjoy.cloud.security.service.UserService;
import net.bestjoy.cloud.security.web.system.response.*;
import net.bestjoy.cloud.security.web.system.request.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static net.bestjoy.cloud.security.core.error.AuthErrors.*;


/***
 * bop后台管理页面
 * @author ray
 */
@Api("权限系统管理api-v1")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@Slf4j
@RequestMapping("v1/sys")
public class SystemAdminController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private PermissionSubjectContext permissionSubjectContext;

    @IgnoreAuth(pathInfo = "/v1/sys/subject/role/list")
    @GetMapping("subject/role/list")
    @ApiOperation("获取权限主体需要的角色列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<RoleResponse>> listRequireRoleOfPermissionSubject(
            @RequestParam String subjectId,
            @RequestParam PermissionTypeEnum permissionType) {
        PermissionSubjectProvider permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(permissionType);
        return Result.success(RoleResponse.convert(permissionSubjectProvider.getSubjectRequireRoleList(subjectId)));
    }

    @IgnoreAuth(pathInfo = "/v1/sys/subject/permission/list")
    @GetMapping("subject/permission/list")
    @ApiOperation("获取权限主体所属的权限列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<PermissionResponse>> listPermissionOfPermissionSubject(
            @RequestParam String subjectId,
            @RequestParam PermissionTypeEnum permissionType) {
        PermissionSubjectProvider permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(permissionType);
        return Result.success(PermissionResponse.convert(permissionSubjectProvider.getSubjectPermissionList(subjectId)));
    }


    @PostMapping("operation/saveOrUpdate")
    @ApiOperation("新增或者更新操作")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<OperationResponse> saveOrUpdateOperation(SaveOrUpdateOperationRequest saveOrUpdateOperationRequest) {
        Operation operation;
        if (StringUtils.isEmpty(saveOrUpdateOperationRequest.getOperationId())) {
            //新增
            operation = permissionService.getOperationByName(saveOrUpdateOperationRequest.getOperationName());
            if (operation != null) {
                throw new BusinessException(OPERATION_ALREADY_EXIST_ERROR);
            }

            operation = new Operation();
            BeanUtils.copyProperties(saveOrUpdateOperationRequest, operation);
            permissionService.saveOperation(operation);
        } else {
            //更新
            operation = permissionService.getOperationById(saveOrUpdateOperationRequest.getOperationId());

            if (operation == null) {
                throw new BusinessException(OPERATION_NOT_FOUND_ERROR);
            }
            BeanUtils.copyProperties(saveOrUpdateOperationRequest, operation);
            permissionService.updateOperation(operation);
        }

        return Result.success(OperationResponse.convert(operation));
    }

    @PostMapping("permission/operation/set")
    @ApiOperation("权限操作设置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setPermissionOperation(PermissionOperationSetRequest permissionOperationSetRequest) {
        Optional<Permission> optionalPermission = Optional.ofNullable(permissionService.getPermissionByPermissionId(permissionOperationSetRequest.getPermissionId()));
        optionalPermission.orElseThrow(() -> new BusinessException(PERMISSION_NOT_FOUND_ERROR));

        Optional<Operation> optionalOperation = Optional.ofNullable(permissionService.getOperationById(permissionOperationSetRequest.getOperationId()));
        optionalOperation.orElseThrow(() -> new BusinessException(OPERATION_NOT_FOUND_ERROR));

        permissionService.addPermissionOperation(optionalPermission.get().getPermissionId(), optionalOperation.get().getOperationId());
        return Result.success(Boolean.TRUE);
    }

    @DeleteMapping("permission/operation/delete")
    @ApiOperation("权限操作删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deletePermissionOperation(PermissionOperationDeleteRequest permissionOperationDeleteRequest) {
        permissionService.deletePermissionOperation(permissionOperationDeleteRequest.getPermissionId(), permissionOperationDeleteRequest.getOperationId());
        return Result.success(Boolean.TRUE);
    }

    @GetMapping("role/permission/query")
    @ApiOperation("角色权限列表查询")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageData<PermissionResponse>> queryRolePermissionList(@RequestParam String roleId, PageBean<Permission> pageBean) {
        IPage<Permission> permissionIPage = permissionService.queryRolePermission(pageBean.getPage(), roleId);

        if (permissionIPage == null || CollectionUtils.isEmpty(permissionIPage.getRecords())) {
            return Result.success(PageData.emptyResult());
        }

        List<PermissionResponse> list = new ArrayList<>();
        permissionIPage.getRecords().forEach(permission -> {
            list.add(PermissionResponse.convert(permission));
        });

        return Result.success(PageData.builderPageData(list,
                PageInfo.builder().current(permissionIPage.getCurrent())
                        .size(permissionIPage.getSize())
                        .total(permissionIPage.getTotal()).build()));
    }

    @PostMapping("menu/permission/set")
    @ApiOperation("目录权限设计")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setMenuPermission(MenuPermissionSetRequest menuPermissionSetRequest) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);
        permissionSubjectProvider.addPermissionSubjectRel(menuPermissionSetRequest.getMenuId(), menuPermissionSetRequest.getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @GetMapping("menu/published/tree")
    @ApiOperation("已发布目录树形结构")
    public Result<List<MenuTreeResponse>> menuTree(
            String parentMenuId, MenuTypeEnum menuType) {

        if (menuType == null) {
            menuType = MenuTypeEnum.HEADER;
        }

        List<Menu> publishedMenuList = permissionService.getPublishedMenuList(parentMenuId, menuType);

        List<MenuResponse> topMenuList = new ArrayList<>();

        if (CollectionUtils.isEmpty(publishedMenuList)) {
            return Result.success(topMenuList);
        }

        Map<String, List<MenuResponse>> subMenuMap = new HashMap<>(16);

        for (Menu menu : publishedMenuList) {
            if (menu.getParentMenuId() == null
                    || menu.getParentMenuId().equals(parentMenuId)) {
                //top menu
                topMenuList.add(MenuResponse.convert(menu));
                continue;
            }

            if (subMenuMap.containsKey(menu.getParentMenuId())) {
                subMenuMap.get(menu.getParentMenuId()).add(MenuResponse.convert(menu));
            } else {
                subMenuMap.put(menu.getParentMenuId(), new ArrayList<MenuResponse>() {{
                    add(MenuResponse.convert(menu));
                }});
            }
        }

        List<MenuTreeResponse> result = new ArrayList<>();
        topMenuList.forEach(topMenu -> {
            MenuTreeResponse topMenuResponse = buildMenuTreeResponse(topMenu, subMenuMap);
            topMenuResponse.setTopMenu(true);
            result.add(topMenuResponse);
        });

        return Result.success(result);
    }

    /**
     * 构建menu tree返回结果
     *
     * @param menuResponse
     * @param subMenuMap
     * @return
     */
    private MenuTreeResponse buildMenuTreeResponse(MenuResponse menuResponse, Map<String, List<MenuResponse>> subMenuMap) {
        MenuTreeResponse menuTreeResponse = new MenuTreeResponse();
        BeanUtils.copyProperties(menuResponse, menuTreeResponse);
        //获取该目录下的子目录
        List<MenuResponse> subMenuList = subMenuMap.get(menuTreeResponse.getMenuId());
        if (!CollectionUtils.isEmpty(subMenuList)) {
            menuTreeResponse.setHasSubMenu(true);
            List<MenuTreeResponse> subMenuTreeList = new ArrayList<>();
            subMenuList.forEach(subMenu -> {
                subMenuTreeList.add(buildMenuTreeResponse(subMenu, subMenuMap));
            });
            menuTreeResponse.setSubMenus(subMenuTreeList);
        }
        return menuTreeResponse;
    }

    @DeleteMapping("menu/permission/delete")
    @ApiOperation("目录权限删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteMenuPermission(MenuPermissionDeleteRequest menuPermissionDeleteRequest) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);
        permissionSubjectProvider.deletePermissionSubjectRel(menuPermissionDeleteRequest.getMenuId(), menuPermissionDeleteRequest.getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("menu/saveOrUpdate")
    @ApiOperation("新增活更新目录")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<MenuResponse> saveOrUpdateMenu(SaveOrUpdateMenuRequest saveOrUpdateMenuRequest) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);
        Menu menu;
        if (StringUtils.isEmpty(saveOrUpdateMenuRequest.getMenuId())) {
            //新增
            menu = permissionSubjectProvider.getPermissionSubjectByName(saveOrUpdateMenuRequest.getMenuName());

            if (menu != null) {
                throw new BusinessException(MENU_ALREADY_EXIST_ERROR);
            }

            menu = new Menu();
            BeanUtils.copyProperties(saveOrUpdateMenuRequest, menu);
            permissionSubjectProvider.savePermissionSubject(menu);
        } else {
            //更新
            menu = permissionSubjectProvider.getPermissionSubject(saveOrUpdateMenuRequest.getMenuId());

            if (menu == null) {
                throw new BusinessException(MENU_NOT_FOUND_ERROR);
            }
            BeanUtils.copyProperties(saveOrUpdateMenuRequest, menu);
            permissionSubjectProvider.updatePermissionSubject(menu);
        }
        return Result.success(MenuResponse.convert(menu));
    }

    @PostMapping("menu/publish")
    @ApiOperation("发布目录")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> publishMenu(String menuId) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);

        Optional<Menu> menuOptional = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(menuId));
        menuOptional.orElseThrow(() -> new BusinessException(MENU_NOT_FOUND_ERROR));
        menuOptional.ifPresent(menu -> {
            menu.setMenuStatus(MenuStatusEnum.PUBLISHED);
            permissionSubjectProvider.updatePermissionSubject(menu);

        });
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("role/permission/set")
    @ApiOperation("角色权限设置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setRolePermission(RolePermissionSetRequest rolePermissionSetRequest) {
        Optional<Role> optionalRole = Optional.ofNullable(permissionService.getRoleById(rolePermissionSetRequest.getRoleId()));
        optionalRole.orElseThrow(() -> new BusinessException(ROLE_NOT_FOUND_ERROR));

        Optional<Permission> optionalPermission = Optional.ofNullable(permissionService.getPermissionByPermissionId(rolePermissionSetRequest.getPermissionId()));
        optionalPermission.orElseThrow(() -> new BusinessException(PERMISSION_NOT_FOUND_ERROR));

        permissionService.addRolePermission(optionalRole.get().getRoleId(), optionalPermission.get().getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("role/permission/delete")
    @ApiOperation("角色权限删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteRolePermission(RolePermissionDeleteRequest rolePermissionDeleteRequest) {
        permissionService.deleteRolePermission(rolePermissionDeleteRequest.getRoleId(), rolePermissionDeleteRequest.getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("permission/saveOrUpdate")
    @ApiOperation("保存或更新权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PermissionResponse> saveOrUpdatePermission(SaveOrUpdatePermissionRequest saveOrUpdatePermissionRequest) {
        Permission permission;

        if (StringUtils.isEmpty(saveOrUpdatePermissionRequest.getPermissionId())) {
            permission = permissionService.getPermissionByName(saveOrUpdatePermissionRequest.getPermissionName());
            if (permission != null) {
                throw new BusinessException(PERMISSION_ALREADY_EXIST_ERROR);
            }

            permission = new Permission();
            BeanUtils.copyProperties(saveOrUpdatePermissionRequest, permission);
            permissionService.savePermission(permission, saveOrUpdatePermissionRequest.getPermissionType(), saveOrUpdatePermissionRequest.getSubjectId(), saveOrUpdatePermissionRequest.getOperationId());
        } else {
            permission = permissionService.getPermissionByPermissionId(saveOrUpdatePermissionRequest.getPermissionId());
            if (permission == null) {
                throw new BusinessException(PERMISSION_NOT_FOUND_ERROR);
            }
            BeanUtils.copyProperties(saveOrUpdatePermissionRequest, permission);
            permissionService.updatePermission(permission);
        }

        return Result.success(PermissionResponse.convert(permission));
    }

    @PostMapping("role/saveOrUpdate")
    @ApiOperation("新增或更新角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<RoleResponse> saveOrUpdateRole(SaveOrUpdateRoleRequest saveOrUpdateRoleRequest) {
        Role role;
        if (StringUtils.isEmpty(saveOrUpdateRoleRequest.getRoleId())) {
            //新增
            role = permissionService.getRoleByName(saveOrUpdateRoleRequest.getRoleName());
            if (role != null) {
                throw new BusinessException(ROLE_ALREADY_EXIST_ERROR);
            }

            role = new Role();
            BeanUtils.copyProperties(saveOrUpdateRoleRequest, role);
            permissionService.addRole(role);
        } else {
            role = permissionService.getRoleById(saveOrUpdateRoleRequest.getRoleId());

            if (role == null) {
                throw new BusinessException(ROLE_NOT_FOUND_ERROR);
            }

            BeanUtils.copyProperties(saveOrUpdateRoleRequest, role);
            permissionService.updateRole(role);
        }

        return Result.success(RoleResponse.convert(role));
    }

    @DeleteMapping("role/delete")
    @ApiOperation("删除角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteRole(@RequestParam String roleId) {
        permissionService.deleteRole(roleId);
        return Result.success(Boolean.TRUE);
    }

    @GetMapping("role/get")
    @ApiOperation("查找角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<RoleResponse> getRole(@RequestParam String roleId) {
        return Result.success(RoleResponse.convert(permissionService.getRoleById(roleId)));
    }

    @PostMapping("user/role/set")
    @ApiOperation("设置用户角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setUserRole(UserRoleSetRequest userRoleSetRequest) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserByUserId(userRoleSetRequest.getUserId()));
        optionalUser.orElseThrow(() -> new BusinessException(USER_NOT_FOUND_ERROR));

        Optional<Role> optionalRole = Optional.ofNullable(permissionService.getRoleById(userRoleSetRequest.getRoleId()));
        optionalRole.orElseThrow(() -> new BusinessException(ROLE_NOT_FOUND_ERROR));

        UserRole userRole = new UserRole(optionalUser.get().getUserId(), optionalRole.get().getRoleId());
        userService.addUserRole(userRole);

        return Result.success(Boolean.TRUE);
    }

    @GetMapping("user/role/list")
    @ApiOperation("用户角色列表，不分页")
    public Result<List<RoleResponse>> getUserRoleList(@RequestParam String userId) {
        return Result.success(RoleResponse.convert(userService.getUserRoles(userId)));
    }

    @DeleteMapping("user/role/delete")
    @ApiOperation("删除用户角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteUserRole(UserRoleDeleteRequest userRoleDeleteRequest) {
        userService.deleteUserRole(userRoleDeleteRequest.getUserId(), userRoleDeleteRequest.getRoleId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("element/saveOrUpdate")
    @ApiOperation("保存或更新页面元素")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<ElementResponse> saveOrUpdateElement(SaveOrUpdateElementRequest saveOrUpdateElementRequest) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);

        Element element;
        if (StringUtils.isEmpty(saveOrUpdateElementRequest.getElementId())) {
            //新增
            element = permissionSubjectProvider.getPermissionSubjectByName(saveOrUpdateElementRequest.getElementName());

            if (element != null) {
                throw new BusinessException(ELEMENT_ALREADY_EXIST_ERROR);
            }

            element = new Element();
            BeanUtils.copyProperties(saveOrUpdateElementRequest, element);
            permissionSubjectProvider.savePermissionSubject(element);
        } else {
            //修改
            element = permissionSubjectProvider.getPermissionSubject(saveOrUpdateElementRequest.getElementId());

            if (element == null) {
                throw new BusinessException(ELEMENT_NOT_FOUND_ERROR);
            }

            BeanUtils.copyProperties(saveOrUpdateElementRequest, element);
            permissionSubjectProvider.updatePermissionSubject(element);
        }

        return Result.success(ElementResponse.convert(element));
    }

    @PostMapping("element/permission/set")
    @ApiOperation("页面元素权限设置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setElementPermission(ElementPermissionSetRequest elementPermissionSetRequest) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);

        Optional<Element> optionalElement = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(elementPermissionSetRequest.getElementId()));
        optionalElement.orElseThrow(() -> new BusinessException(ELEMENT_NOT_FOUND_ERROR));

        Optional<Permission> optionalPermission = Optional.ofNullable(permissionService.getPermissionByPermissionId(elementPermissionSetRequest.getPermissionId()));
        optionalPermission.orElseThrow(() -> new BusinessException(PERMISSION_NOT_FOUND_ERROR));

        permissionSubjectProvider.addPermissionSubjectRel(optionalElement.get().getElementId(), optionalPermission.get().getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @DeleteMapping("element/permission/delete")
    @ApiOperation("页面元素权限删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteElementPermission(ElementPermissionDeleteRequest elementPermissionDeleteRequest) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);
        permissionSubjectProvider.deletePermissionSubjectRel(elementPermissionDeleteRequest.getElementId(), elementPermissionDeleteRequest.getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @DeleteMapping("menu/delete")
    @ApiOperation("删除菜单")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteMenu(@RequestParam String menuId) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);

        Optional<Menu> menuOptional = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(menuId));
        menuOptional.orElseThrow(() -> new BusinessException(MENU_NOT_FOUND_ERROR));

        menuOptional.ifPresent(menu -> {
            permissionSubjectProvider.deleteSubject(menu.getMenuId());
        });
        return Result.success(Boolean.TRUE);
    }

    @DeleteMapping("element/delete")
    @ApiOperation("删除页面元素")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteElement(@RequestParam String elementId) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);

        Optional<Element> elementOptional = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(elementId));
        elementOptional.orElseThrow(() -> new BusinessException(ELEMENT_NOT_FOUND_ERROR));

        elementOptional.ifPresent(element -> {
            permissionSubjectProvider.deleteSubject(element.getElementId());
        });
        return Result.success(Boolean.TRUE);
    }
}
