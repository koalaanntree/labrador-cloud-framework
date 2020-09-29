package net.bestjoy.cloud.security.web.auth.controller;

import io.swagger.annotations.Api;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.constant.SecurityConstant;
import net.bestjoy.cloud.security.context.SecurityContext;
import net.bestjoy.cloud.security.converter.UserConverter;
import net.bestjoy.cloud.security.core.AppUserDetails;
import net.bestjoy.cloud.security.core.UserSession;
import net.bestjoy.cloud.security.core.entitiy.User;
import net.bestjoy.cloud.security.core.enums.UserStateEnum;
import net.bestjoy.cloud.security.core.enums.UserTypeEnum;
import net.bestjoy.cloud.security.core.error.UserAlreadyExistException;
import net.bestjoy.cloud.security.service.UserService;
import net.bestjoy.cloud.security.util.JwtHelper;
import net.bestjoy.cloud.security.web.auth.request.RegisterUserRequest;
import net.bestjoy.cloud.security.web.auth.request.UserLoginRequest;
import net.bestjoy.cloud.security.web.auth.response.LoginResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.bestjoy.cloud.security.core.error.AuthErrors.USER_PASSWORD_INCORRECT;


/***
 * 授权
 * @author ray
 */
@Api
@RestController
@RequestMapping("/v1/security/auth")
public class SecurityAuthController {

    @Autowired
    private AuthenticationProvider authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private SecurityContextLogoutHandler securityContextLogoutHandler;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/token/refresh")
    public Result<Boolean> refreshToken() {
        //todo

        return null;
    }

    @PostMapping("logout")
    public Result<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        securityContextLogoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return Result.success(Boolean.TRUE);
    }

    @GetMapping("userinfo")
    public Result<UserSession> getUserInfo() {
        UserSession userSession = SecurityContext.getCurrentUser();
        //todo 获取用户的详情信息
        return Result.success(userSession);
    }


    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(
            @RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        return Result.success(
                doLogin(userLoginRequest.getUsername(), userLoginRequest.getPassword(), response));
    }

    private LoginResponse doLogin(String username, String password, HttpServletResponse response) {
        // 通过用户名和密码创建一个 Authentication 认证对象，实现类为 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        //通过 AuthenticationManager（默认实现为ProviderManager）的authenticate方法验证 Authentication 对象
        String token = null;
        AppUserDetails userDetails = null;
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            token = jwtHelper.createToken(authentication);

            response.setHeader(SecurityConstant.AUTH_HEADER, token);
            userDetails = (AppUserDetails) authentication.getDetails();
            //跨域需要加此header，不然拿不到token
            response.setHeader("Access-Control-Expose-Headers", SecurityConstant.AUTH_HEADER);
        } catch (BadCredentialsException bE) {
            throw new BusinessException(USER_PASSWORD_INCORRECT);
        }
        LoginResponse loginResponse = new LoginResponse();
        if (userDetails != null) {
            loginResponse = UserConverter.INSTANCE.userToLoginResponse(userDetails);
        }
        loginResponse.setToken(token);
        return loginResponse;
    }

    @PostMapping("/registerAndLogin")
    public Result<LoginResponse> registerAndLogin(
            RegisterUserRequest registerUserRequest, HttpServletResponse response) {

        User user = userService.getUserByUsername(registerUserRequest.getUsername());

        if (user != null) {
            throw new UserAlreadyExistException();
        }

        //todo 密码校验

        user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getConfirmedPassword()));
        user.setUserState(UserStateEnum.NORMAL);
        //默认注册普通用户
        if (registerUserRequest.getUserType() == null) {
            user.setUserType(UserTypeEnum.NORMAL);
        }
        BeanUtils.copyProperties(registerUserRequest, user);

        userService.saveUser(user);

        return Result.success(doLogin(user.getUsername(), registerUserRequest.getConfirmedPassword(), response));
    }
}
