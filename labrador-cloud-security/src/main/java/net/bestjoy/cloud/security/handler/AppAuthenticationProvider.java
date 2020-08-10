package net.bestjoy.cloud.security.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.core.AppUserDetails;
import net.bestjoy.cloud.security.service.impl.AppUserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/***
 * @author ray
 */
@Slf4j
@RequiredArgsConstructor
public class AppAuthenticationProvider implements AuthenticationProvider {

    @NonNull
    private AppUserDetailsServiceImpl appUserDetailsService;

    @NonNull
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        AppUserDetails userDetails = (AppUserDetails) appUserDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(userDetails);

            //登录成功
            //将 Authentication 绑定到 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            return usernamePasswordAuthenticationToken;
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
