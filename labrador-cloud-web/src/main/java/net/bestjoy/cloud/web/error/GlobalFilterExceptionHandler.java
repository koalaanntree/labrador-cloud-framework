package net.bestjoy.cloud.web.error;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.error.bean.BusinessException;
import net.bestjoy.cloud.error.bean.SysException;
import net.bestjoy.cloud.web.util.WebUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * filter 全局异常处理
 * @author ray
 */
@Slf4j
public class GlobalFilterExceptionHandler implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (log.isDebugEnabled()) {
            log.info("filter全局异常处理器初始化...");
        }
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BusinessException be) {
            WebUtil.returnObjectResponse(httpServletResponse, Result.fail(be.getError()));
        } catch (SysException sysE) {
            WebUtil.returnObjectResponse(httpServletResponse, Result.fail(sysE.getError()));
        }
    }
}
