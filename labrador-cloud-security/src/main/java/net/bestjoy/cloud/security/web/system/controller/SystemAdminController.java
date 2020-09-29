package net.bestjoy.cloud.security.web.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.PageBean;
import net.bestjoy.cloud.core.bean.PageData;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessAssert;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.core.dto.QueryMenuDTO;
import net.bestjoy.cloud.security.core.dto.QueryOperationDTO;
import net.bestjoy.cloud.security.core.dto.QueryRoleDTO;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.MenuStatusEnum;
import net.bestjoy.cloud.security.core.enums.MenuTypeEnum;
import net.bestjoy.cloud.security.core.enums.PermissionTypeEnum;
import net.bestjoy.cloud.security.service.PermissionService;
import net.bestjoy.cloud.security.service.PermissionSubjectContext;
import net.bestjoy.cloud.security.service.PermissionSubjectProvider;
import net.bestjoy.cloud.security.service.UserService;
import net.bestjoy.cloud.security.web.system.converter.SystemDataConverter;
import net.bestjoy.cloud.security.web.system.response.*;
import net.bestjoy.cloud.security.web.system.request.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("subject/role/list")
    @ApiOperation("获取权限主体需要的角色列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<RoleVO>> listRequireRoleOfPermissionSubject(
            @RequestParam String subjectId,
            @RequestParam PermissionTypeEnum permissionType) {
        PermissionSubjectProvider permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(permissionType);

        Optional<List<Role>> optional = Optional.ofNullable(permissionSubjectProvider.getSubjectRequireRoleList(subjectId));

        return optional.map(roles -> Result.success(roles.stream().map(SystemDataConverter.INSTATNCE::roleToVO).collect(Collectors.toList()))).orElseGet(() -> Result.success(new ArrayList<>()));
    }

    @GetMapping("subject/permission/list")
    @ApiOperation("获取权限主体所属的权限列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<PermissionVO>> listPermissionOfPermissionSubject(
            @RequestParam String subjectId,
            @RequestParam PermissionTypeEnum permissionType) {
        PermissionSubjectProvider permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(permissionType);

        Optional<List<Permission>> optional = Optional.ofNullable(permissionSubjectProvider.getSubjectPermissionList(subjectId));

        return optional.map(
                permissions -> Result.success(permissions.stream().map(SystemDataConverter.INSTATNCE::permissionToVO).collect(Collectors.toList()))
        ).orElseGet(() -> Result.success(new ArrayList<>()));
    }

    @ApiOperation("操作列表查询")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("operation/list")
    public Result<PageData<OperationVO>> listOperations(
            PageBean<Operation> pageBean, QueryOperationDTO queryOperationDTO) {

        Optional<IPage<Operation>> operation = Optional.ofNullable(permissionService.queryOperation(pageBean.getPage(), queryOperationDTO));

        return operation.map((pageResult) -> Result.success(PageData.buildResult(pageResult, SystemDataConverter.INSTATNCE::operationToVO)))
                .orElseGet(() -> Result.success(PageData.emptyResult()));
    }

    @ApiOperation("物理删除操作")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("operation/delete")
    public Result<Boolean> delOperation(String operationId) {
        permissionService.deleteOperation(operationId);

        return Result.success(Boolean.TRUE);
    }


    @PostMapping("operation/saveOrUpdate")
    @ApiOperation("新增或者更新操作")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<OperationVO> saveOrUpdateOperation(
            @Valid SaveOrUpdateOperationRequest saveOrUpdateOperationRequest) {
        Operation operation;
        if (StringUtils.isEmpty(saveOrUpdateOperationRequest.getOperationId())) {
            //新增
            operation = permissionService.getOperationByCode(saveOrUpdateOperationRequest.getOperationCode());

            if (operation != null) {
                log.warn("operation add,operation already exist:{}", saveOrUpdateOperationRequest.getOperationCode());
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

        return Result.success(SystemDataConverter.INSTATNCE.operationToVO(operation));
    }

    @PostMapping("permission/operation/set")
    @ApiOperation("权限操作设置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setPermissionOperation(
            @Valid PermissionOperationSetRequest permissionOperationSetRequest) {
        Optional<Permission> optionalPermission = Optional.ofNullable(permissionService.getPermissionByPermissionId(permissionOperationSetRequest.getPermissionId()));

        return optionalPermission.map(permission -> {
            Optional<Operation> optionalOperation = Optional.ofNullable(permissionService.getOperationById(permissionOperationSetRequest.getOperationId()));

            if (!optionalOperation.isPresent()) {
                log.warn("set permission operation, operation not found:{}", permissionOperationSetRequest.getOperationId());
                throw new BusinessException(OPERATION_NOT_FOUND_ERROR);
            }

            return optionalOperation.map(operation -> {
                permissionService.addPermissionOperation(permission.getPermissionId(), operation.getOperationId());
                return Result.success(Boolean.TRUE);
            }).get();
        }).orElseThrow(() -> {
            log.warn("set permission operation, permission not found:{}", permissionOperationSetRequest.getPermissionId());
            return new BusinessException(PERMISSION_NOT_FOUND_ERROR);
        });
    }

    @DeleteMapping("permission/operation/delete")
    @ApiOperation("权限操作删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deletePermissionOperation(
            @Valid PermissionOperationDeleteRequest permissionOperationDeleteRequest) {
        permissionService.deletePermissionOperation(permissionOperationDeleteRequest.getPermissionId(), permissionOperationDeleteRequest.getOperationId());
        return Result.success(Boolean.TRUE);
    }

    @GetMapping("role/permission/query")
    @ApiOperation("角色权限列表查询")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageData<PermissionVO>> queryRolePermissionList(@RequestParam String roleId, PageBean<Permission> pageBean) {
        return Result.success(
                PageData.buildResult(permissionService.queryRolePermission(pageBean.getPage(), roleId), SystemDataConverter.INSTATNCE::permissionToVO));
    }

    @GetMapping("menu/list")
    @ApiOperation("菜单列表，不区分状态，可单级查询")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageData<MenuVO>> queryMenuList(PageBean<Menu> pageBean, QueryMenuDTO queryMenuDTO) {
        return Result.success(
                PageData.buildResult(permissionService.queryMenu(pageBean.getPage(), queryMenuDTO), SystemDataConverter.INSTATNCE::menuToVO));
    }

    @PostMapping("menu/permission/set")
    @ApiOperation("目录权限设计")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setMenuPermission(
            @Valid MenuPermissionSetRequest menuPermissionSetRequest) {
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

        List<MenuVO> topMenuList = new ArrayList<>();
        List<MenuTreeResponse> result = new ArrayList<>();

        if (CollectionUtils.isEmpty(publishedMenuList)) {
            return Result.success(result);
        }

        Map<String, List<MenuVO>> subMenuMap = new HashMap<>(16);

        for (Menu menu : publishedMenuList) {
            if (menu.getParentMenuId() == null
                    || menu.getParentMenuId().equals(parentMenuId)) {
                //top menu
                topMenuList.add(SystemDataConverter.INSTATNCE.menuToVO(menu));
                continue;
            }

            if (subMenuMap.containsKey(menu.getParentMenuId())) {
                subMenuMap.get(menu.getParentMenuId()).add(SystemDataConverter.INSTATNCE.menuToVO(menu));
            } else {
                subMenuMap.put(menu.getParentMenuId(), new ArrayList<MenuVO>() {{
                    add(SystemDataConverter.INSTATNCE.menuToVO(menu));
                }});
            }
        }

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
    private MenuTreeResponse buildMenuTreeResponse(MenuVO menuResponse, Map<String, List<MenuVO>> subMenuMap) {
        MenuTreeResponse menuTreeResponse = new MenuTreeResponse();
        BeanUtils.copyProperties(menuResponse, menuTreeResponse);
        //获取该目录下的子目录
        List<MenuVO> subMenuList = subMenuMap.get(menuTreeResponse.getMenuId());
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
    public Result<Boolean> deleteMenuPermission(@Valid MenuPermissionDeleteRequest menuPermissionDeleteRequest) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);
        permissionSubjectProvider.deletePermissionSubjectRel(menuPermissionDeleteRequest.getMenuId(), menuPermissionDeleteRequest.getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("menu/saveOrUpdate")
    @ApiOperation("新增活更新目录")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<MenuVO> saveOrUpdateMenu(
            @Valid SaveOrUpdateMenuRequest saveOrUpdateMenuRequest) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);
        Menu menu;
        if (StringUtils.isEmpty(saveOrUpdateMenuRequest.getMenuId())) {
            //新增
            menu = permissionSubjectProvider.getPermissionSubjectByCode(saveOrUpdateMenuRequest.getMenuCode());

            if (menu != null) {
                log.warn("add menu already exist:: menuCode:{}", saveOrUpdateMenuRequest.getMenuCode());
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
        return Result.success(SystemDataConverter.INSTATNCE.menuToVO(menu));
    }

    @PostMapping("menu/publish")
    @ApiOperation("发布目录")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> publishMenu(String menuId) {
        PermissionSubjectProvider<Menu> permissionSubjectProvider =
                permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.MENU);

        Optional<Menu> menuOptional = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(menuId));

        return menuOptional.map(menu -> {
            menu.setMenuStatus(MenuStatusEnum.PUBLISHED);
            permissionSubjectProvider.updatePermissionSubject(menu);
            return Result.success(Boolean.TRUE);
        }).orElseThrow(() -> {
            log.warn("publish menu ,menu not found:{}", menuId);
            return new BusinessException(MENU_NOT_FOUND_ERROR);
        });
    }

    @PostMapping("role/permission/set")
    @ApiOperation("角色权限设置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setRolePermission(
            @Valid RolePermissionSetRequest rolePermissionSetRequest) {
        Optional<Role> optionalRole = Optional.ofNullable(permissionService.getRoleById(rolePermissionSetRequest.getRoleId()));

        return optionalRole.map(role -> {
            Optional<Permission> optionalPermission = Optional.ofNullable(permissionService.getPermissionByPermissionId(rolePermissionSetRequest.getPermissionId()));

            if (!optionalPermission.isPresent()) {
                log.warn("role permission set, permission not found:{}", rolePermissionSetRequest.getPermissionId());
                throw new BusinessException(PERMISSION_NOT_FOUND_ERROR);
            }

            return optionalPermission.map(permission -> {
                permissionService.addRolePermission(role.getRoleId(), permission.getPermissionId());
                return Result.success(Boolean.TRUE);
            }).get();
        }).orElseThrow(() -> {
            log.warn("role permission set, role not found:{}", rolePermissionSetRequest.getRoleId());
            return new BusinessException(ROLE_NOT_FOUND_ERROR);
        });
    }

    @PostMapping("role/permission/delete")
    @ApiOperation("角色权限删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteRolePermission(
            @Valid RolePermissionDeleteRequest rolePermissionDeleteRequest) {
        permissionService.deleteRolePermission(rolePermissionDeleteRequest.getRoleId(), rolePermissionDeleteRequest.getPermissionId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("permission/saveOrUpdate")
    @ApiOperation("保存或更新权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PermissionVO> saveOrUpdatePermission(
            @Valid SaveOrUpdatePermissionRequest saveOrUpdatePermissionRequest) {
        Permission permission;

        if (StringUtils.isEmpty(saveOrUpdatePermissionRequest.getPermissionId())) {
            permission = permissionService.getPermissionByCode(saveOrUpdatePermissionRequest.getPermissionCode());
            if (permission != null) {
                log.warn("add permission, permission already exist:{}", saveOrUpdatePermissionRequest.getPermissionCode());
                throw new BusinessException(PERMISSION_ALREADY_EXIST_ERROR);
            }

            BusinessAssert.notNull(saveOrUpdatePermissionRequest.getPermissionType(), "permissionType is null.");
            BusinessAssert.notBlank(saveOrUpdatePermissionRequest.getOperationId(), "operationId is null.");
            BusinessAssert.notBlank(saveOrUpdatePermissionRequest.getSubjectId(), "subjectId is null");

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

        if (!StringUtils.isEmpty(saveOrUpdatePermissionRequest.getRoleId())) {
            log.info("relate role:{}, permission:{}", saveOrUpdatePermissionRequest.getRoleId(), saveOrUpdatePermissionRequest.getPermissionId());

            permissionService.addRolePermission(saveOrUpdatePermissionRequest.getRoleId(), permission.getPermissionId());
        }

        return Result.success(permission, SystemDataConverter.INSTATNCE::permissionToVO);
    }

    @PostMapping("role/saveOrUpdate")
    @ApiOperation("新增或更新角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<RoleVO> saveOrUpdateRole(
            @Valid SaveOrUpdateRoleRequest saveOrUpdateRoleRequest) {
        Role role;
        if (StringUtils.isEmpty(saveOrUpdateRoleRequest.getRoleId())) {
            //新增
            role = permissionService.getRoleByCode(saveOrUpdateRoleRequest.getRoleCode());
            if (role != null) {
                log.warn("role add, role already exist:{}", saveOrUpdateRoleRequest.getRoleCode());
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

        return Result.success(SystemDataConverter.INSTATNCE.roleToVO(role));
    }

    @DeleteMapping("role/delete")
    @ApiOperation("删除角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteRole(@RequestParam String roleId) {
        Optional<Role> optionalRole = Optional.ofNullable(permissionService.getRoleById(roleId));

        return optionalRole.map(role -> {
            permissionService.deleteRole(role.getRoleId());
            return Result.success(Boolean.TRUE);
        }).orElseThrow(() -> {
            log.warn("delete role, role not found:{}", roleId);
            return new BusinessException(ROLE_NOT_FOUND_ERROR);
        });
    }

    @GetMapping("role/get")
    @ApiOperation("查找角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<RoleVO> getRole(@RequestParam String roleId) {
        return Result.success(SystemDataConverter.INSTATNCE.roleToVO(permissionService.getRoleById(roleId)));
    }


    @GetMapping("role/list/query")
    @ApiOperation("分页查询角色列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageData<RoleVO>> pageRoles(QueryRoleDTO queryRoleDTO, PageBean<Role> pageBean) {
        return Result.success(
                PageData.buildResult(permissionService.pageQueryRoles(queryRoleDTO, pageBean), SystemDataConverter.INSTATNCE::roleToVO));
    }

    @PostMapping("user/role/set")
    @ApiOperation("设置用户角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setUserRole(
            @Valid UserRoleSetRequest userRoleSetRequest) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserByUserId(userRoleSetRequest.getUserId()));

        return optionalUser.map(user -> {
            Optional<Role> optionalRole = Optional.ofNullable(permissionService.getRoleById(userRoleSetRequest.getRoleId()));

            if (!optionalRole.isPresent()) {
                log.warn("set user role, role not found:{}", userRoleSetRequest.getRoleId());
                throw new BusinessException(ROLE_NOT_FOUND_ERROR);
            }

            return optionalRole.map(role -> {
                UserRole userRole = new UserRole(user.getUserId(), role.getRoleId());
                userService.addUserRole(userRole);
                return Result.success(Boolean.TRUE);
            }).get();
        }).orElseThrow(() -> {
            log.warn("set user role, user not found:{}", userRoleSetRequest.getUserId());
            return new BusinessException(USER_NOT_FOUND_ERROR);
        });

    }

    @GetMapping("user/role/list")
    @ApiOperation("用户角色列表，不分页")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<RoleVO>> getUserRoleList(@RequestParam String userId) {

        Optional<List<Role>> optional = Optional.ofNullable(userService.getUserRoles(userId));

        return optional.map(roles -> Result.success(roles.stream().map(SystemDataConverter.INSTATNCE::roleToVO).collect(Collectors.toList())))
                .orElseGet(() -> Result.success(new ArrayList<>()));
    }

    @DeleteMapping("user/role/delete")
    @ApiOperation("删除用户角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteUserRole(
            @Valid UserRoleDeleteRequest userRoleDeleteRequest) {
        userService.deleteUserRole(userRoleDeleteRequest.getUserId(), userRoleDeleteRequest.getRoleId());
        return Result.success(Boolean.TRUE);
    }

    @PostMapping("element/saveOrUpdate")
    @ApiOperation("保存或更新页面元素")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<ElementVO> saveOrUpdateElement(
            @Valid SaveOrUpdateElementRequest saveOrUpdateElementRequest) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);

        Element element;
        if (StringUtils.isEmpty(saveOrUpdateElementRequest.getElementId())) {
            //新增
            element = permissionSubjectProvider.getPermissionSubjectByCode(saveOrUpdateElementRequest.getElementName());

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

        return Result.success(SystemDataConverter.INSTATNCE.elementToVO(element));
    }

    @PostMapping("element/permission/set")
    @ApiOperation("页面元素权限设置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> setElementPermission(
            @Valid ElementPermissionSetRequest elementPermissionSetRequest) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);

        Optional<Element> optionalElement = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(elementPermissionSetRequest.getElementId()));

        return optionalElement.map(element -> {
            //设置element permission rel
            Optional<Permission> optionalPermission =
                    Optional.ofNullable(permissionService.getPermissionByPermissionId(elementPermissionSetRequest.getPermissionId()));

            if (!optionalPermission.isPresent()) {
                log.warn("set element permission, permission not found:{}", elementPermissionSetRequest.getPermissionId());
                throw new BusinessException(PERMISSION_NOT_FOUND_ERROR);
            }

            return optionalPermission.map(permission -> {
                permissionSubjectProvider.addPermissionSubjectRel(element.getElementId(), permission.getPermissionId());
                return Result.success(Boolean.TRUE);
            }).get();
        }).orElseThrow(() -> {
            log.warn("set element permission, menu not found:{}", elementPermissionSetRequest.getElementId());
            return new BusinessException(ELEMENT_NOT_FOUND_ERROR);
        });
    }

    @DeleteMapping("element/permission/delete")
    @ApiOperation("页面元素权限删除")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteElementPermission(
            @Valid ElementPermissionDeleteRequest elementPermissionDeleteRequest) {
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

        return menuOptional.map(menu -> {
            permissionSubjectProvider.deleteSubject(menu.getMenuId());
            return Result.success(Boolean.TRUE);
        }).orElseThrow(() -> {
            log.warn("delete menu not found:{}", menuId);
            return new BusinessException(MENU_NOT_FOUND_ERROR);
        });
    }

    @DeleteMapping("element/delete")
    @ApiOperation("删除页面元素")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteElement(@RequestParam String elementId) {
        PermissionSubjectProvider<Element> permissionSubjectProvider = permissionSubjectContext.getPermissionSubjectProvider(PermissionTypeEnum.ELEMENT);

        Optional<Element> elementOptional = Optional.ofNullable(permissionSubjectProvider.getPermissionSubject(elementId));
        return elementOptional.map(element -> {
            permissionSubjectProvider.deleteSubject(element.getElementId());
            return Result.success(Boolean.TRUE);
        }).orElseThrow(() -> {
            log.warn("delete element :{} not found", elementId);
            return new BusinessException(ELEMENT_NOT_FOUND_ERROR);
        });
    }
}
