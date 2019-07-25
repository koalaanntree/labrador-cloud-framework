package net.bestjoy.cloud.logger.filter;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.logger.context.LoggerContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 拦截http 请求，设置traceId
 * @author ray
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public final class TraceLoggerContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("当前traceId:{}", LoggerContext.getTraceId());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
