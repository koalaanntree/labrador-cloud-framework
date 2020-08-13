package net.bestjoy.cloud.security.web.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.core.UserSession;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.service.PermissionService;
import net.bestjoy.cloud.security.service.UserService;
import net.bestjoy.cloud.security.web.system.response.PermissionResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("permission/check")
    @ApiOperation("当前用户权限(权限名、单个)检查")
    public Result<Boolean> checkPermission(@RequestParam String permissionName) {
        Optional<Permission> permissionOption = Optional.ofNullable(permissionService.getPermissionByName(permissionName));
        permissionOption.orElseThrow(() -> new BusinessException(PERMISSION_NOT_FOUND_ERROR));

        UserSession userSession = SecurityContext.getCurrentUser();

        List<String> permissionIdList = userService.getUserPermissionIdList(userSession.getUserId());

        //判断当前用户是否拥有角色权限
        if (permissionIdList.contains(permissionOption.get().getPermissionId())) {
            return Result.success(Boolean.TRUE);
        }

        return Result.success(Boolean.FALSE);
    }

    @GetMapping("permission/list")
    @ApiOperation("当前用户权限列表")
    public Result<List<PermissionResponse>> getUserPermissionList() {
        UserSession userSession = SecurityContext.getCurrentUser();

        List<Permission> permissionList = userService.getUserPermissionList(userSession.getUserId());
        List<PermissionResponse> responseList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            permissionList.forEach(permission -> {
                responseList.add(PermissionResponse.convert(permission));
            });
        }

        return Result.success(responseList);
    }
}
