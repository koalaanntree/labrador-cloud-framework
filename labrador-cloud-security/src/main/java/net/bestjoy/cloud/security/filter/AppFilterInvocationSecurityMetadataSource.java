package net.bestjoy.cloud.security.filter;

import net.bestjoy.cloud.security.context.SecurityContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

import static net.bestjoy.cloud.security.constant.SecurityConstant.ROLE_ANONYMOUS;


/***
 * 自定义url对应的权限
 * todo
 * @author ray
 */
public class AppFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AppFilterInvocationSecurityMetadataSource(
            FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) o;
        String url = filterInvocation.getRequest().getRequestURI();

        //ignore url 添加ROLE_ANONYMOUS访问权限
        if (!CollectionUtils.isEmpty(SecurityContext.getSecurityExcludePathList())) {
            for (String path : SecurityContext.getSecurityExcludePathList()) {
                if (antPathMatcher.match(path, url)) {
                    return SecurityConfig.createList(ROLE_ANONYMOUS);
                }
            }
        }

        return securityMetadataSource.getAttributes(o);
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
