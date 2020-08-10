package net.bestjoy.cloud.security.handler;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.bean.Result;
import net.bestjoy.cloud.web.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 登出成功处理类
 * @author ray
 *
 */
@Slf4j
@Component
public class AppLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.info("登出成功");
        }

        //返回登出成功结果页面
        WebUtil.returnObjectResponse(httpServletResponse, Result.success(new Result("A00000", "登出成功")));
    }
}
