package net.bestjoy.cloud.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static net.bestjoy.cloud.security.constant.SecurityConstant.PRINCIPAL_ANONYMOUS;
import static net.bestjoy.cloud.security.constant.SecurityConstant.ROLE_ANONYMOUS;

/***
 * 自定义访问决策器
 * @author ray
 */
@Component
@Slf4j
public class AppAccessDecisionManager implements AccessDecisionManager {

    /***
     *
     * @param authentication 登录凭证
     * @param o 访问的url
     * @param collection url对应的权限列表
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        //url需要的权限角色
        List<String> needRoleList = new ArrayList<>();
        for (Iterator<ConfigAttribute> iter = collection.iterator(); iter.hasNext(); ) {
            String needRole = iter.next().getAttribute();

            if (!StringUtils.isEmpty(needRole)) {
                needRoleList.add(needRole.trim());
            }
        }

        if (allowAccess(needRoleList, authentication)) {
            return;
        }

        throw new AccessDeniedException("当前访问没有权限");
    }

    /***
     * 判断是否有权限访问
     *   这里采取的默认策略是：如果含有里面一个角色即允许访问
     * @param needRoleList
     * @param authentication
     * @return
     */
    private boolean allowAccess(List<String> needRoleList, Authentication authentication) {
        //如果是url是匿名角色，那么都允许访问，约定普通用户都默认还有匿名用户的角色
        if (!CollectionUtils.isEmpty(needRoleList)) {
            for (String needRole : needRoleList) {
                if (ROLE_ANONYMOUS.equals(needRole)) {
                    return true;
                }
            }
        }

        if (!PRINCIPAL_ANONYMOUS.equals(authentication.getPrincipal())) {
            //如果是登录状态，那么无特殊权限要求的url，允许访问
            if (CollectionUtils.isEmpty(needRoleList)) {
                return true;
            }

            //有特殊角色请求的url，判断用户是否含有该角色
            for (String needRole : needRoleList) {
                for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    if (grantedAuthority.getAuthority().trim().equalsIgnoreCase(needRole)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 表示此AccessDecisionManager是否能够处理传递的ConfigAttribute呈现的授权请求
     */
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    /**
     * 表示当前AccessDecisionManager实现是否能够为指定的安全对象（方法调用或Web请求）提供访问控制决策
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
