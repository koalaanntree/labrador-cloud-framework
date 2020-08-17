package net.bestjoy.cloud.security.context;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.converter.UserConverter;
import net.bestjoy.cloud.security.core.AppUserDetails;
import net.bestjoy.cloud.security.core.UserSession;
import net.bestjoy.cloud.security.core.error.UserNeedLoginException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/***
 * bop security context
 * @author ray
 */
@Slf4j
@Component
public final class SecurityContext {
    /***
     * 系统id
     */
    private  static String systemId;
    /***
     * 授权过滤url
     */
    private final static List<String> securityExcludePathList = new ArrayList<>();

    /***
     * 获取当前登录用户id
     * @return
     */
    public static String getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    /***
     * 获取当前登录用户
     * @return
     */
    public static UserSession getCurrentUser() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("用户未登录，或已过期");
            throw new UserNeedLoginException();
        }

        AppUserDetails userDetails = (AppUserDetails) authentication.getDetails();

        if (userDetails == null) {
            log.warn("用户未登录，或已过期");
            throw new UserNeedLoginException();
        }

        UserSession userSession = UserConverter.INSTANCE.detailToSession(
                userDetails);
        userSession.setRoles(AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        return userSession;
    }


    public static void addSecurityExcludePath(String path) {
        securityExcludePathList.add(path);
    }

    public static List<String> getSecurityExcludePathList() {
        return securityExcludePathList;
    }

    /**
     * 清空
     */
    public static void clearSecurityExcludePath() {
        securityExcludePathList.clear();
    }

    /***
     * 移除path
     * @param path
     */
    public static void removeSecurityExcludePath(String path) {
        if (!securityExcludePathList.contains(path)) {
            return;
        }

        securityExcludePathList.remove(path);
    }

    public static boolean isContainSecurityExcludePath(String path) {
        return securityExcludePathList.contains(path);
    }


    public static String getSystemId() {
        return systemId;
    }

    public static void setSystemId(String systemId) {
        SecurityContext.systemId = systemId;
    }
}
