package net.bestjoy.cloud.security.filter;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.constant.SecurityConstant;
import net.bestjoy.cloud.security.core.error.UserNeedLoginException;
import net.bestjoy.cloud.security.util.JwtHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 解析token
 * @author ray
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private JwtHelper jwtHelper;

    public JwtAuthenticationTokenFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        if (log.isDebugEnabled()) {
            log.info("jwt auth filter init...");
        }
        super.initFilterBean();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authToken = httpServletRequest.getHeader(SecurityConstant.AUTH_HEADER);

        if (!StringUtils.isEmpty(authToken) && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtHelper.validateToken(authToken)) {
                Authentication authentication = jwtHelper.getAuthentication(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("用户登录filter，凭证已过期");
                throw new UserNeedLoginException();
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
