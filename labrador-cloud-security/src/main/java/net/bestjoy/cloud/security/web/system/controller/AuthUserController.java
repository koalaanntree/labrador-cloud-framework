package net.bestjoy.cloud.security.web.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bestjoy.cloud.core.bean.PageBean;
import net.bestjoy.cloud.core.bean.PageData;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.converter.UserConverter;
import net.bestjoy.cloud.security.core.UserSession;
import net.bestjoy.cloud.security.core.dto.QueryUserDTO;
import net.bestjoy.cloud.security.core.dto.UserDTO;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.service.PermissionService;
import net.bestjoy.cloud.security.service.UserService;
import net.bestjoy.cloud.security.web.system.converter.SystemDataConverter;
import net.bestjoy.cloud.security.web.system.response.PermissionVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.bestjoy.cloud.security.core.error.AuthErrors.PERMISSION_NOT_FOUND_ERROR;


/**
 * @author ray
 */
@Api("用户api-v1")
@RestController
@RequestMapping("v1/user")
public class AuthUserController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private UserService userService;


    @GetMapping("list")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("用户列表（管理员权限）")
    public Result<PageData<UserDTO>> listUser(QueryUserDTO queryUserDTO, PageBean pageBean) {
        IPage<User> pageUser = userService.pageQueryUser(pageBean, queryUserDTO);

        PageData<UserDTO> data = PageData.buildResult(pageUser, UserConverter.INSTANCE::userToDTO);
        return Result.success(data);
    }

    @GetMapping("permission/check")
    @ApiOperation("当前用户权限(权限名、单个)检查")
    public Result<Boolean> checkPermission(@RequestParam String permissionName) {
        Optional<Permission> permissionOption = Optional.ofNullable(permissionService.getPermissionByName(permissionName));
        permissionOption.orElseThrow(() -> new BusinessException(PERMISSION_NOT_FOUND_ERROR));

        UserSession userSession = SecurityContext.getCurrentUser();

        Optional<List<String>> optional = Optional.ofNullable(userService.getUserPermissionIdList(userSession.getUserId()));

        return optional
                //判断当前用户是否拥有角色权限
                .filter(permissionIds -> permissionIds.contains(permissionOption.get().getPermissionId()))
                .map(permissionIds -> Result.success(Boolean.TRUE))
                .orElseGet(() -> Result.success(Boolean.FALSE));
    }

    @GetMapping("permission/list")
    @ApiOperation("当前用户权限列表")
    public Result<List<PermissionVO>> getUserPermissionList() {
        UserSession userSession = SecurityContext.getCurrentUser();

        Optional<List<Permission>> optional = Optional.ofNullable(userService.getUserPermissionList(userSession.getUserId()));

        return optional.map(permissions -> Result.success(
                permissions.stream().map(SystemDataConverter.INSTATNCE::permissionToVO).collect(Collectors.toList())))
                .orElseGet(() -> Result.success(new ArrayList<>()));

    }
}
