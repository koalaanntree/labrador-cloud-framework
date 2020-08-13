package net.bestjoy.cloud.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.converter.UserConverter;
import net.bestjoy.cloud.security.core.AppUserDetails;
import net.bestjoy.cloud.security.core.entitiy.*;
import net.bestjoy.cloud.security.core.enums.UserStateEnum;
import net.bestjoy.cloud.security.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static net.bestjoy.cloud.security.core.error.AuthErrors.USER_NOT_FOUND_ERROR;
import static net.bestjoy.cloud.security.core.error.AuthErrors.USER_STATE_ERROR;


/***
 * spring security 持久层查询用户接口实现
 * @author ray
 */
@Slf4j
@Service("appUserDetailsService")
public class AppUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInfo = userService.getUserByUsername(username);

        if (userInfo == null) {
            //跑出用户不存在异常
            log.warn("user:{} not found.", username);
            throw new BusinessException(USER_NOT_FOUND_ERROR);
        }

        if (!UserStateEnum.NORMAL.equals(userInfo.getUserState())) {
            //用户状态异常
            log.warn("user state:{} error.", userInfo.getUserState());
            throw new BusinessException(USER_STATE_ERROR);
        }

        List<Role> userRoles = userService.getUserRoles(userInfo.getUserId());
        List<GrantedAuthority> userAuthorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoles)) {
            for (Role role : userRoles) {
                userAuthorityList.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
        }
        AppUserDetails appUserDetails = UserConverter.INSTANCE.userToDetail(userInfo);

        if (!CollectionUtils.isEmpty(userAuthorityList)) {
            appUserDetails.setAuthorities(userAuthorityList);
        }
        return appUserDetails;
    }
}
