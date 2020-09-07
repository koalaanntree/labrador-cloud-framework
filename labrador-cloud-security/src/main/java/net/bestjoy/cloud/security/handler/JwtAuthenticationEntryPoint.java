package net.bestjoy.cloud.security.handler;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.core.error.BusinessException;
import net.bestjoy.cloud.security.core.error.UserNeedLoginException;
import net.bestjoy.cloud.web.util.WebUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException, BusinessException {

        if (log.isDebugEnabled()) {
            log.info("authentication entry...");
        }

        WebUtil.returnObjectResponse(httpServletResponse, Result.fail(new UserNeedLoginException().getError()));
    }
}
